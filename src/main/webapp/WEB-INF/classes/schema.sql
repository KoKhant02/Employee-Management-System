CREATE TABLE IF NOT EXISTS Department (
    id INT AUTO_INCREMENT PRIMARY KEY,
    Code VARCHAR(100) UNIQUE,
    Name VARCHAR(100) UNIQUE
);


CREATE TABLE IF NOT EXISTS Employee (
    id INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(50),
    Email VARCHAR(100) UNIQUE,
    Phone VARCHAR(20),
    Salary DECIMAL(10, 2),
    Username VARCHAR(50) UNIQUE,
    Password VARCHAR(50),
    DepartmentID INT,
    FOREIGN KEY (DepartmentID) REFERENCES Department(id) ON DELETE SET NULL,
    role VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS Positions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(100) UNIQUE
);

CREATE TABLE IF NOT EXISTS Employee_Position (
    EmployeeID INT,
    PositionID INT,
    PRIMARY KEY (EmployeeID, PositionID),
    FOREIGN KEY (EmployeeID) REFERENCES Employee(id) ON DELETE CASCADE,
    FOREIGN KEY (PositionID) REFERENCES Positions(id) ON DELETE CASCADE
);