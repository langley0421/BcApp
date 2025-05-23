-- databaseを作成
-- CREATE DATABASE IF NOT EXISTS db_cardApp;


-- databaseを使用
-- USE db_cardApp;


-- databaseのcard tableを作成
-- CREATE TABLE IF NOT EXISTS card (
--     id INT AUTO_INCREMENT PRIMARY KEY,
--     company VARCHAR(100) NOT NULL,
--     name VARCHAR(20) NOT NULL,
--     zipcode VARCHAR(10),
--     address VARCHAR(30),
--     department VARCHAR(20),
--     phone VARCHAR(20),
--     position VARCHAR(20),
--     email VARCHAR(50),
--     remarks TEXT,
--     favorite BOOLEAN DEFAULT FALSE,
--     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
-- );

-- databaseのuser tableを作成
-- CREATE TABLE IF NOT EXISTS user (
--     id INT AUTO_INCREMENT PRIMARY KEY,
--     email VARCHAR(50) NOT NULL,
--     password VARCHAR(255) NOT NULL
-- );

-- DROP TABLE IF EXISTS card;


-- CREATE TABLE IF NOT EXISTS company (
--     company_id INT AUTO_INCREMENT PRIMARY KEY,
--     company_name VARCHAR(50) NOT NULL,
--     zipcode VARCHAR(10),
--     address VARCHAR(30),
--     phone VARCHAR(20)
-- );

-- -- department テーブルを作成
-- CREATE TABLE IF NOT EXISTS department (
--     department_id INT AUTO_INCREMENT PRIMARY KEY,
--     department_name VARCHAR(20) NOT NULL
-- );

-- -- position テーブルを作成
-- CREATE TABLE IF NOT EXISTS `position` (
--     position_id INT AUTO_INCREMENT PRIMARY KEY,
--     position_name VARCHAR(20) NOT NULL
-- );

-- -- card テーブルを作成
-- CREATE TABLE IF NOT EXISTS card (
--     card_id INT AUTO_INCREMENT PRIMARY KEY,
--     company_id INT NOT NULL,
--     department_id INT NOT NULL,
--     position_id INT NOT NULL,
--     name VARCHAR(20) NOT NULL,
--     email VARCHAR(50),
--     remarks TEXT,
--     favorite BOOLEAN DEFAULT FALSE,
--     created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
--     FOREIGN KEY (company_id) REFERENCES company(company_id),
--     FOREIGN KEY (department_id) REFERENCES department(department_id),
--     FOREIGN KEY (position_id) REFERENCES `position`(position_id)
-- );