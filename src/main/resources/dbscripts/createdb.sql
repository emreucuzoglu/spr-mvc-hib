CREATE SCHEMA `hibnatedb` DEFAULT CHARACTER SET utf8 COLLATE utf8_turkish_ci ;

CREATE USER 'hibuser'@'localhost' IDENTIFIED BY 'root';
GRANT USAGE ON *.* TO 'hibuser'@'localhost';
GRANT ALL PRIVILEGES ON `hibnatedb`.* TO 'hibuser'@'localhost';