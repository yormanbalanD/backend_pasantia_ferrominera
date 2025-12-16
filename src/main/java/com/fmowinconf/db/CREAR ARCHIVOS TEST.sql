INSERT INTO archivo(id, id_respaldo, id_padre, nombre_archivo, es_carpeta, ruta, extension, tamaño) VALUES
-- NIVEL 0: Carpetas y archivos en la raíz (id_padre es NULL)
(1, 1, NULL, 'Documentos', 1, 'C:\Users\Analista\Documentos', '', '0KB'),
(2, 1, NULL, 'Descargas', 1, 'C:\Users\Analista\Descargas', '', '0KB'),
(3, 1, NULL, 'notas_rapidas.txt', 0, 'C:\Users\Analista\', 'txt', '1KB'),
(4, 1, NULL, 'config.ini', 0, 'C:\Users\Analista\', 'ini', '4KB'),

-- NIVEL 1: Archivos dentro de la carpeta 'Documentos' (id_padre = 1)
(5, 1, 1, 'Curriculum.pdf', 0, 'C:\Users\Analista\Documentos\', 'pdf', '450KB'),
(6, 1, 1, 'Presupuesto_2025.xlsx', 0, 'C:\Users\Analista\Documentos\', 'xlsx', '2.1MB'),
(7, 1, 1, 'Proyectos', 1, 'C:\Users\Analista\Documentos\Proyectos', '', '0KB'), -- Subcarpeta

-- NIVEL 1: Archivos dentro de la carpeta 'Descargas' (id_padre = 2)
(8, 1, 2, 'instalador_chrome.exe', 0, 'C:\Users\Analista\Descargas\', 'exe', '85MB'),
(9, 1, 2, 'imagen_graciosa.jpg', 0, 'C:\Users\Analista\Descargas\', 'jpg', '3MB'),

-- NIVEL 2: Archivos dentro de la subcarpeta 'Proyectos' (id_padre = 7)
(10, 1, 7, 'Diagrama_ER.png', 0, 'C:\Users\Analista\Documentos\Proyectos\', 'png', '1.2MB'),
(11, 1, 7, 'codigo_fuente.zip', 0, 'C:\Users\Analista\Documentos\Proyectos\', 'zip', '15MB');