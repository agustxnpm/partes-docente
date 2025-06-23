-- Script para agregar 20 personas adicionales
-- Evitando conflictos con los DNI ya utilizados en los tests

-- Personas adicionales
INSERT INTO persona (dni, nombre, apellido, cuil, sexo, titulo, domicilio, telefono) VALUES 
(11111111, 'Carlos', 'Mendoza', '27-11111111-9', 'M', 'Profesor de Matemática', 'Mitre 789', '+54 (280) 411-1234'),
(22222222, 'Ana', 'Vásquez', '20-22222222-9', 'F', 'Profesora de Lengua', 'Rivadavia 456', '+54 (280) 422-5678'),
(33333333, 'Luis', 'Fernández', '27-33333333-9', 'M', 'Profesor de Historia', 'San Martín 123', '+54 (280) 433-9012'),
(44444444, 'María', 'García', '20-44444444-9', 'F', 'Profesora de Biología', 'Belgrano 789', '+54 (280) 444-3456'),
(55555555, 'Roberto', 'Silva', '27-55555555-9', 'M', 'Profesor de Física', 'Sarmiento 321', '+54 (280) 455-7890'),
(66666666, 'Carmen', 'López', '20-66666666-9', 'F', 'Profesora de Química', 'Moreno 654', '+54 (280) 466-1234'),
(77777777, 'Diego', 'Martín', '27-77777777-9', 'M', 'Profesor de Geografía', 'Alem 987', '+54 (280) 477-5678'),
(12345678, 'Lucía', 'Rodríguez', '20-12345678-9', 'F', 'Profesora de Arte', 'Independencia 147', '+54 (280) 412-3456'),
(23456789, 'Marcos', 'González', '27-23456789-9', 'M', 'Profesor de Educación Física', '25 de Mayo 258', '+54 (280) 423-4567'),
(34567890, 'Silvia', 'Herrera', '20-34567890-9', 'F', 'Profesora de Inglés', 'Libertad 369', '+54 (280) 434-5678'),
(45678901, 'Andrés', 'Ruiz', '27-45678901-9', 'M', 'Profesor de Tecnología', 'Rawson 741', '+54 (280) 445-6789'),
(56789012, 'Patricia', 'Morales', '20-56789012-9', 'F', 'Profesora de Música', 'Maiz 852', '+54 (280) 456-7890'),
(67890123, 'Fernando', 'Castro', '27-67890123-9', 'M', 'Profesor de Informática', 'Rosa 963', '+54 (280) 467-8901'),
(78901234, 'Valeria', 'Díaz', '20-78901234-9', 'F', 'Profesora de Filosofía', 'Charcas 159', '+54 (280) 478-9012'),
(89012345, 'Sebastián', 'Jiménez', '27-89012345-9', 'M', 'Profesor de Psicología', 'Jujuy 357', '+54 (280) 489-0123'),
(90123456, 'Gabriela', 'Vargas', '20-90123456-9', 'F', 'Profesora de Economía', 'Zar 468', '+54 (280) 490-1234'),
(10987654, 'Nicolás', 'Peña', '27-10987654-9', 'M', 'Profesor de Contabilidad', 'Roca 579', '+54 (280) 410-9876'),
(21098765, 'Cristina', 'Romero', '20-21098765-9', 'F', 'Profesora de Derecho', 'La Pampa 680', '+54 (280) 421-0987'),
(32109876, 'Esteban', 'Aguirre', '27-32109876-9', 'M', 'Profesor de Sociología', 'Mitre 791', '+54 (280) 432-1098'),
(43210987, 'Mónica', 'Sánchez', '20-43210987-9', 'F', 'Profesora de Literatura', 'Rivadavia 802', '+54 (280) 443-2109');
