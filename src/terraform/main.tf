provider "aws" {
    region = "us-east-1"
    access_key = var.aws_access_key
    secret_key = var.aws_secret_key
}

resource "aws_instance" "database" {
  ami           = var.ami_id 
  instance_type = var.instance_type
  subnet_id = var.subnet_id
  security_groups = var.security_group_ids
  associate_public_ip_address = true 
  key_name = "terr-ansible-pipeline"

  user_data = <<-EOF
                #!/bin/bash
                sudo apt update -y
                sudo apt install -y mysql-server
                sudo systemctl start mysql
                
                CONFIG_FILE="/etc/mysql/mysql.conf.d/mysqld.cnf"
                sudo sed -i "s/^bind-address.*/bind-address            = 0.0.0.0/" "$CONFIG_FILE"
                sudo sed -i "s/^mysqlx-bind-address.*/mysqlx-bind-address     = 0.0.0.0/" "$CONFIG_FILE"
                sudo systemctl restart mysql
            
                # inner heredoc
                sudo mysql -u root <<SQL_SCRIPT
                CREATE USER IF NOT EXISTS 'admin'@'%' IDENTIFIED BY 'admin';
                GRANT ALL PRIVILEGES ON *.* TO 'admin'@'%' WITH GRANT OPTION;
                FLUSH PRIVILEGES;
                SQL_SCRIPT
                
                sudo systemctl restart mysql
            EOF
  tags = {
    Name = "database"
  }
}

resource "aws_instance" "backend" {
  ami           = var.ami_id
  instance_type = var.instance_type
  subnet_id = var.subnet_id
  security_groups = var.security_group_ids
  associate_public_ip_address = true
  key_name = "terr-ansible-pipeline"

  user_data = <<-EOF
              #!/bin/bash
                sudo apt update -y
                sudo apt install -y openjdk-21-jre-headless
              EOF
  tags = {
    Name = "backend"
  }
}

resource "aws_instance" "frontend" {
  ami           = var.ami_id
  instance_type = var.instance_type
  subnet_id = var.subnet_id
  security_groups = var.security_group_ids
  associate_public_ip_address = true
  key_name = "terr-ansible-pipeline"

  user_data = <<-EOF
              #!/bin/bash
                sudo apt update -y
              EOF
  tags = {
    Name = "frontend"
  }
}

resource "local_file" "ansible_inventory" {
  filename = "../ansible/inventory.ini"
  content  = <<EOT
[frontend]
${aws_instance.frontend.public_ip}

[backend]
${aws_instance.backend.public_ip}

[database]
${aws_instance.database.public_ip}
EOT

  # This ensures the file is only written after all three instances are up
  depends_on = [
    aws_instance.frontend,
    aws_instance.backend,
    aws_instance.database
  ]
}
