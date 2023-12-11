INSERT INTO USERS (username, password, email, created_on) VALUES ('admin', '$2a$10$2dnbN.uDbYTGj2RRb2TAn.M4DWPpp0C5uo/2QUDOD5Hfl8Jx6wj6C', 'admin@email.com', CURRENT_TIMESTAMP);
INSERT INTO USERS (username, password, email, created_on) VALUES ('elcinBN1', '$2a$10$2dnbN.uDbYTGj2RRb2TAn.M4DWPpp0C5uo/2QUDOD5Hfl8Jx6wj6C', 'admin1@email.com', CURRENT_TIMESTAMP);
INSERT INTO USERS (username, password, email, created_on) VALUES ('petia123', '$2a$10$2dnbN.uDbYTGj2RRb2TAn.M4DWPpp0C5uo/2QUDOD5Hfl8Jx6wj6C', 'admin2@email.com', CURRENT_TIMESTAMP);
INSERT INTO USERS (username, password, email, created_on) VALUES ('kolya1978', '$2a$10$2dnbN.uDbYTGj2RRb2TAn.M4DWPpp0C5uo/2QUDOD5Hfl8Jx6wj6C', 'admin3@email.com', CURRENT_TIMESTAMP);
INSERT INTO USERS (username, password, email, created_on) VALUES ('ivan.Ag007', '$2a$10$2dnbN.uDbYTGj2RRb2TAn.M4DWPpp0C5uo/2QUDOD5Hfl8Jx6wj6C', 'admin4@email.com', CURRENT_TIMESTAMP);
INSERT INTO USERS (username, password, email, created_on) VALUES ('bobr_forest', '$2a$10$2dnbN.uDbYTGj2RRb2TAn.M4DWPpp0C5uo/2QUDOD5Hfl8Jx6wj6C', 'admin5@email.com', CURRENT_TIMESTAMP);
INSERT INTO USERS (username, password, email, created_on) VALUES ('shevchuk', '$2a$10$2dnbN.uDbYTGj2RRb2TAn.M4DWPpp0C5uo/2QUDOD5Hfl8Jx6wj6C', 'admin6@email.com', CURRENT_TIMESTAMP);

INSERT INTO ROLES (name, user_id) VALUES ('ADMIN', 1);

INSERT INTO STATUS (name) VALUES ('OPEN');
INSERT INTO STATUS (name) VALUES ('IN PROGRESS');
INSERT INTO STATUS (name) VALUES ('COMPLETED');
INSERT INTO STATUS (name) VALUES ('CANCELLED');

INSERT INTO PRIORITY (name) VALUES ('HIGH');
INSERT INTO PRIORITY (name) VALUES ('MIDDLE');
INSERT INTO PRIORITY (name) VALUES ('LOW');

INSERT INTO TASKS (title, description, author_id, executor_id, status_id, priority_id, created_on, last_updated_on) VALUES ('Admin Task!', 'TODO New service', 1, 2, 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO TASKS (title, description, author_id, executor_id, status_id, priority_id, created_on, last_updated_on) VALUES ('Admin Task!', 'TODO New service', 1, 3, 2, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO TASKS (title, description, author_id, executor_id, status_id, priority_id, created_on, last_updated_on) VALUES ('Elcin Task!', 'TODO New service', 2, 1, 1, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO TASKS (title, description, author_id, executor_id, status_id, priority_id, created_on, last_updated_on) VALUES ('Elcin Task!', 'TODO New service', 2, 1, 2, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO TASKS (title, description, author_id, executor_id, status_id, priority_id, created_on, last_updated_on) VALUES ('Petia Task!', 'TODO New service', 3, 1, 3, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO COMMENTS (text, task_id, author_id, created_on, last_updated_on) VALUES ('Admin comment 1', 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO COMMENTS (text, task_id, author_id, created_on, last_updated_on) VALUES ('Admin comment 2', 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO COMMENTS (text, task_id, author_id, created_on, last_updated_on) VALUES ('Elcin comment 3', 2, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO COMMENTS (text, task_id, author_id, created_on, last_updated_on) VALUES ('Petia comment 4', 3, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
