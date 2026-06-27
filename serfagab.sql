-- Borramos la bd si existe
DROP DATABASE IF EXISTS serfagab;
-- Creamos la bd
CREATE DATABASE serfagab;

-- Activamos la bd
USE serfagab;

create table tbl_tipo(
    id_tipo     int primary key,
    descripcion varchar(15) not null
);
insert into tbl_tipo values (1, 'administrador');

create table tbl_usuario (
    id_usuario int auto_increment primary key,
    nombres varchar(50) not null,
    apellidos varchar(50) not null,
    login varchar(50) not null unique,
    clave varchar(100) not null,
    email varchar(100) not null,
    activo bit default 1,
    id_tipo int default 1,
    constraint fk_usuario_tipo foreign key (id_tipo) references tbl_tipo(id_tipo)
);
insert into tbl_usuario values (null, 'Valerie', 'Cavero', 'admin', '123', 'vcavero@serfagab.com', default, 1);

create table tbl_proveedor(
    id_proveedor int auto_increment primary key,
    razon_social varchar(150) not null,
    ruc char(11) not null,
    celular varchar(20),
    email varchar(100),
    descripcion varchar(255),
    activo bit default 1
);
insert into tbl_proveedor values 
(null,'Comercializadora Andina EIRL','20605478931','987456321','ventas@andina.com.pe','Proveedor de materiales de construccion',default),
(null,'Distribuidora Industrial Peru E.I.R.L.','20587412366','956741258','contacto@dipindustrial.pe','Venta de equipos electronicos',default);

create table tbl_tipo_material(
    id_tipo_material int auto_increment primary key,
    nombre varchar(100) not null,
    descripcion varchar(255),
    activo bit default 1
);
insert into tbl_tipo_material values (1,'Plancha','Son materiales metalicos para tablero',default);
insert into tbl_tipo_material values (2,'Pintura','Gris ANSI 61',default);

create table tbl_material(
    id_material int auto_increment primary key,
    id_tipo_material int not null,
    nombre varchar(150) not null,
    unidad_medida varchar(20) not null,
    stock_actual decimal(10,2) default 0.00,
    precio_referencial decimal(10,2) default 0.00,
    descripcion varchar(255),
    activo bit default 1,
    constraint fk_material_tipo foreign key (id_tipo_material) references tbl_tipo_material(id_tipo_material)
);
insert into tbl_material values (null,1,'Perno 3x3','unidad',10,3,'para tablero',default);
insert into tbl_material values (null,2,'Perno 4x3','unidad',12,4,'para tablero',default);

create table tbl_orden_compra(
    id_orden_compra int auto_increment primary key,
    id_proveedor int not null,
    fecha date not null,
    estado varchar(20) default 'PENDIENTE',
    total decimal(12,2) default 0.00,
    observaciones varchar(255),
    constraint fk_orden_proveedor foreign key (id_proveedor) references tbl_proveedor(id_proveedor)
);
insert into tbl_orden_compra values
(1,1,'2025-12-10','PENDIENTE',0.00,''),
(2,2,'2025-12-09','PENDIENTE',0.00,''),
(3,1,'2025-12-08','PENDIENTE',0.00,''),
(4,1,'2025-12-09','PENDIENTE',0.00,''),
(5,1,'2025-12-03','PENDIENTE',264.00,''),
(6,1,'2025-12-05','PENDIENTE',105.00,''),
(7,2,'2025-12-09','PENDIENTE',311.00,''),
(8,1,'2025-12-08','PENDIENTE',87.00,'Sin observaciones'),
(9,1,'2025-12-08','PENDIENTE',315.00,''),
(10,2,'2025-12-09','PENDIENTE',750.00,'Compra urgente');

create table tbl_detalle_orden_compra(
    id_detalle int auto_increment primary key,
    id_orden_compra int not null,
    id_material int not null,
    cantidad decimal(10,2) not null,
    precio_unitario decimal(10,2) not null,
    subtotal decimal(12,2),
    constraint fk_detalle_orden foreign key (id_orden_compra) references tbl_orden_compra(id_orden_compra),
    constraint fk_detalle_material foreign key (id_material) references tbl_material(id_material)
);
insert into tbl_detalle_orden_compra values
(null,1,1,5,28,140),
(null,2,2,5,28,140);

ALTER TABLE tbl_orden_compra
ADD COLUMN id_usuario INT NOT NULL DEFAULT 1;

ALTER TABLE tbl_orden_compra
ADD CONSTRAINT fk_orden_usuario
FOREIGN KEY (id_usuario)
REFERENCES tbl_usuario(id_usuario);

CREATE VIEW v_detalle_orden_compra AS
SELECT
    d.id_detalle,
    d.id_orden_compra,
    m.nombre AS material,
    d.cantidad,
    d.precio_unitario,
    d.subtotal
FROM tbl_detalle_orden_compra d
INNER JOIN tbl_material m
ON d.id_material = m.id_material;

CREATE OR REPLACE VIEW v_header_orden_compra AS
SELECT
    oc.id_orden_compra,
    CONCAT('OC-', LPAD(oc.id_orden_compra,5,'0')) AS numeroOrden,
    p.razon_social,
    p.ruc,
    CONCAT(u.nombres,' ',u.apellidos) AS nombreCompletoUsuario,
    oc.fecha,
    DATE_FORMAT(oc.fecha,'%d/%m/%Y') AS fechaTexto,
    oc.estado,
    oc.total,
    oc.observaciones
FROM tbl_orden_compra oc
INNER JOIN tbl_proveedor p
    ON oc.id_proveedor = p.id_proveedor
INNER JOIN tbl_usuario u
    ON oc.id_usuario = u.id_usuario;
