--
-- Script SQL corregido para crear las tablas en SQLite
-- (Usando INTEGER para los campos booleanos)
--

-- 1. Tabla 'analistas'
CREATE TABLE analista (
    id INTEGER PRIMARY KEY,
    nombre_completo TEXT,
    ficha TEXT,
    password TEXT,
	permisos TEXT,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP
);

-- 2. Tabla 'configuracion'
CREATE TABLE configuracion (
    id INTEGER PRIMARY KEY,
    analista_id INTEGER,
    fmo_equipo TEXT,
    crear_usuario INTEGER,         -- Corregido de BOOLEAN a INTEGER (0=FALSE, 1=TRUE)
    mozilla_firefox INTEGER,       -- Corregido de BOOLEAN a INTEGER
    mozilla_thunderbird INTEGER,   -- Corregido de BOOLEAN a INTEGER
    hostname_dominio INTEGER,      -- Corregido de BOOLEAN a INTEGER
    configurar_impresora INTEGER,  -- Corregido de BOOLEAN a INTEGER
    configurar_escaner INTEGER,    -- Corregido de BOOLEAN a INTEGER
    configurar_ip INTEGER,         -- Corregido de BOOLEAN a INTEGER
	sistema_operativo TEXT,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (analista_id) REFERENCES analista(id)
);

-- 3. Tabla 'configuracion_ip'
CREATE TABLE configuracion_ip (
    id INTEGER PRIMARY KEY,
    id_configuraciones INTEGER,
    ip_address TEXT,
    subnet_mask TEXT,
    default_gateway TEXT,
	created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_configuraciones) REFERENCES configuracion(id)
);

-- 4. Tabla 'configuracion_impresora'
CREATE TABLE configuracion_impresora (
    id INTEGER PRIMARY KEY,
    id_configuraciones INTEGER,
    modelo TEXT,
    ip_address TEXT,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_configuraciones) REFERENCES configuracion(id)
);

-- 5. Tabla 'respaldo'
CREATE TABLE respaldo (
    id INTEGER PRIMARY KEY,
    analista_id INTEGER,
    fmo_equipo TEXT,
    sistema_operativo TEXT,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
	completado_con_exito INT,
	tipo TEXT, -- CARGAR, CREAR
	tiempo_start TEXT,
	tiempo_end TEXT,
    FOREIGN KEY (analista_id) REFERENCES analista(id)
);

-- 6. Tabla 'archivo'
CREATE TABLE archivo (
    id INTEGER PRIMARY KEY,
    id_respaldo INTEGER,
    id_padre INTEGER,
    nombre_archivo TEXT,
    es_carpeta TEXT,
    ruta TEXT,
    extension TEXT,
    tamaño TEXT,
	created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_respaldo) REFERENCES respaldo(id),
    FOREIGN KEY (id_padre) REFERENCES archivo(id)
);