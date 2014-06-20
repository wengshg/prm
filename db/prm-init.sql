-- ----------------------------------------------------------------------------
-- PRM MS SERVER SQL SCRIPT
-- V1.0 2014-6-4
-- V1.1 2014-6-11
-- V1.2 2014-6-12
-- V1.2.1 2014-6-15, add workorder_material.tolerance
-- V1.2.2 2014-6-15, add intialized data of weighing_room, workorder_container
-- V1.2.3 2014-6-15, remove NOT NULL for workorder_log.uid
-- ----------------------------------------------------------------------------

-- ----------------------------------------------------------------------------
-- Drop Tables 
-- ----------------------------------------------------------------------------
DROP TABLE store_material
DROP TABLE workorder_log
DROP TABLE workorder_material
DROP TABLE workorder_container
DROP TABLE workorder
DROP TABLE schedule
DROP TABLE process_flow_item
DROP TABLE process_flow
DROP TABLE bom_item;
DROP TABLE bom;
DROP TABLE product;
DROP TABLE material;
DROP TABLE weighing_room;
DROP TABLE equipment_gate
DROP TABLE equipment
DROP TABLE line
DROP TABLE [user]

-- ----------------------------------------------------------------------------
-- Create Tables 
-- ----------------------------------------------------------------------------
CREATE TABLE [user] (
    id int NOT NULL PRIMARY KEY IDENTITY(1,1),
    username varchar(64) NOT NULL unique,
    passwd varchar(64),
    name varchar(64),
    dept varchar(32),
    role varchar(32),
    memo varchar(256),
    enable tinyint
);

CREATE TABLE line(
    id int NOT NULL PRIMARY KEY IDENTITY(1,1),
    code varchar(32) NOT NULL unique,
    name varchar(32) NOT NULL unique,
    quantity float,
    enable tinyint
);

CREATE TABLE equipment(
    id int NOT NULL PRIMARY KEY IDENTITY(1,1),
    lid int NOT NULL constraint fk_equipment_lid foreign key references line(id),
    code varchar(32) NOT NULL unique,
    name varchar(32) NOT NULL unique,
    type varchar(32) NOT NULL,
    enable tinyint
);

CREATE TABLE equipment_gate(
    id int NOT NULL PRIMARY KEY IDENTITY(1,1),
    lid int NOT NULL constraint fk_equipment_gate_lid foreign key references line(id),
    eid int NOT NULL constraint fk_equipment_gate_eid foreign key references equipment(id),
    code varchar(32) NOT NULL unique,
    name varchar(32) NOT NULL unique,
    type varchar(32) NOT NULL,
    enable tinyint
);

CREATE TABLE weighing_room(
    id int NOT NULL PRIMARY KEY IDENTITY(1,1),
    code varchar(32) NOT NULL unique,
    name varchar(32) NOT NULL unique
);

CREATE TABLE product (
    id int NOT NULL PRIMARY KEY IDENTITY(1,1),
    code varchar(32) NOT NULL unique,
    name varchar(32) NOT NULL unique,
    unit varchar(8)
);


CREATE TABLE material (
    id int NOT NULL PRIMARY KEY IDENTITY(1,1),
    code varchar(32) NOT NULL unique,
    name varchar(32) NOT NULL unique,
    type varchar(32),
    container varchar(32),
    unit varchar(8),
    enable tinyint
);

CREATE TABLE bom (
    id int NOT NULL PRIMARY KEY IDENTITY(1,1),
    pid int NOT NULL constraint fk_bom_pid foreign key references product(id),
    code varchar(32) NOT NULL unique,
    name varchar(32) NOT NULL unique,
    quantity float,
    unit varchar(8),
    enable tinyint
);

CREATE TABLE bom_item (
    id int NOT NULL PRIMARY KEY IDENTITY(1,1),
    bid int NOT NULL constraint fk_bom_item_bid foreign key references bom(id),
    mid int NOT NULL constraint fk_bom_item_mid foreign key references material(id),
    pid int NOT NULL constraint fk_bom_item_pid foreign key references product(id),
    quantity float,
    tolerance float,
    unit varchar(8)
);

CREATE TABLE process_flow (
    id int NOT NULL PRIMARY KEY IDENTITY(1,1),
    bid int NOT NULL constraint fk_process_flow_bid foreign key references bom(id),
    lid int NOT NULL constraint fk_process_flow_lid foreign key references line(id),
    pid int NOT NULL constraint fk_process_flow_pid foreign key references product(id),
    code varchar(32) NOT NULL unique,
    name varchar(32) NOT NULL unique,
    type varchar(32),
    enable tinyint
);

CREATE TABLE process_flow_item (
    id int NOT NULL PRIMARY KEY IDENTITY(1,1),
    fid int NOT NULL constraint fk_process_flow_item_fid foreign key references process_flow(id),
    bid int NOT NULL constraint fk_process_flow_item_bid foreign key references bom(id),
    lid int NOT NULL constraint fk_process_flow_item_lid foreign key references line(id),
    pid int NOT NULL constraint fk_process_flow_item_pid foreign key references product(id),
    mid int NOT NULL constraint fk_process_flow_item_mid foreign key references material(id),
    sequence smallint,
    interval smallint,
    eqpt_type varchar(32),
    gate_type varchar(32),
    enable tinyint
);

CREATE TABLE schedule (
    id int NOT NULL PRIMARY KEY IDENTITY(1,1),
    fid int NOT NULL constraint fk_schedule_fid foreign key references process_flow(id),
    bid int NOT NULL constraint fk_schedule_bid foreign key references bom(id),
    lid int NOT NULL constraint fk_schedule_lid foreign key references line(id),
    pid int NOT NULL constraint fk_schedule_pid foreign key references product(id),
    code varchar(32) NOT NULL unique,
    quantity float,
    unit varchar(8),
    schd_sdate bigint,
    schd_edate bigint,
    schd_time bigint,
    schd_uid int NOT NULL constraint fk_schedule_schd_uid foreign key references [user](id),
    appr_time bigint,
    appr_uid int constraint fk_schedule_appr_uid foreign key references [user](id),
);

CREATE TABLE workorder (
    id int NOT NULL PRIMARY KEY IDENTITY(1,1),
    sid int NOT NULL constraint fk_workorder_sid foreign key references schedule(id),
    fid int NOT NULL constraint fk_workorder_fid foreign key references process_flow(id),
    bid int NOT NULL constraint fk_workorder_bid foreign key references bom(id),
    lid int NOT NULL constraint fk_workorder_lid foreign key references line(id),
    pid int NOT NULL constraint fk_workorder_pid foreign key references product(id),
    code varchar(32) NOT NULL unique,
    sequence smallint,
    quantity float,
    unit varchar(8),
    work_sdate bigint, 
    work_edate bigint, 
    status tinyint,
    owner_uid int NOT NULL constraint fk_workorder_owner_uid foreign key references [user](id),
    weighing_uid int constraint fk_workorder_weighing_uid foreign key references [user](id),
    operator_uid int constraint fk_workorder_operator_uid foreign key references [user](id)
);
CREATE index work_sdate_index on workorder(work_sdate);
CREATE index work_edate_index on workorder(work_edate);

CREATE TABLE workorder_container (
    id int NOT NULL PRIMARY KEY IDENTITY(1,1),
    wid int NOT NULL constraint fk_workorder_container_wid foreign key references workorder(id),
    mid int NOT NULL constraint fk_workorder_container_mid foreign key references material(id),
    fid int NOT NULL constraint fk_workorder_container_fid foreign key references process_flow(id),
    bid int NOT NULL constraint fk_workorder_container_bid foreign key references bom(id),
    lid int NOT NULL constraint fk_workorder_container_lid foreign key references line(id),
    pid int NOT NULL constraint fk_workorder_container_pid foreign key references product(id),
    eid int NOT NULL constraint fk_workorder_container_eid foreign key references equipment(id),
    gid int NOT NULL constraint fk_workorder_container_gid foreign key references equipment_gate(id),
    sequence smallint,
    total float,
    quantity float,
    unit varchar(8),
    status tinyint
);

CREATE TABLE workorder_material (
    id int NOT NULL PRIMARY KEY IDENTITY(1,1),
    wid int NOT NULL constraint fk_workorder_material_bid foreign key references workorder(id),
    mid int NOT NULL constraint fk_workorder_material_mid foreign key references material(id),
    actl_total float,
    actl_quantity float,
    quantity float,
    tolerance float,
    container_qty int,
    unit varchar(8),
    status tinyint
);

CREATE TABLE workorder_log (
    id int NOT NULL PRIMARY KEY IDENTITY(1,1),
    wid int NOT NULL constraint fk_workorder_log_bid foreign key references workorder(id),
    mid int constraint fk_workorder_log_mid foreign key references material(id),
    uid int constraint fk_workorder_log_uid foreign key references [user](id),
    sequence smallint,
    status tinyint,
    created_time bigint,
    created_uid int NOT NULL constraint fk_workorder_log_created_uid foreign key references [user](id)
);

CREATE TABLE store_material (
    id int NOT NULL PRIMARY KEY IDENTITY(1,1),
    mid int NOT NULL constraint fk_store_material_mid foreign key references material(id),
    rid int NOT NULL constraint fk_store_material_rid foreign key references weighing_room(id),
    original_code varchar(64),
    quantity float,
    unit varchar(8),
    signed_date bigint,
    signed_uid int NOT NULL constraint fk_store_material_signed_Uid foreign key references [user](id)
);

-- ----------------------------------------------------------------------------
-- End of Initiailized Database Tables
-- ----------------------------------------------------------------------------

-- ----------------------------------------------------------------------------
-- Initiailize Data
-- ----------------------------------------------------------------------------

INSERT INTO [user] (username, passwd, name, dept, role, memo, enable) VALUES ('s-test1', 'spass', '������', '�칫��', '��������', 'memo', 1);
INSERT INTO [user] (username, passwd, name, dept, role, memo, enable) VALUES ('s-test2', 'spass', '�����', '�칫��', '��������', 'memo', 1);
INSERT INTO [user] (username, passwd, name, dept, role, memo, enable) VALUES ('s-test3', 'spass', '����', '���ϲ�', '����Ա', 'memo', 1);
INSERT INTO [user] (username, passwd, name, dept, role, memo, enable) VALUES ('s-test4', 'spass', '����', 'Ʒ����', 'Ʒ��Ա', 'memo', 1);
INSERT INTO [user] (username, passwd, name, dept, role, memo, enable) VALUES ('s-test5', 'spass', '����', '���ϲ�', '����Ա', 'memo', 1);

INSERT INTO [line] (code, name, quantity, enable) VALUES ('s-L001', 's-1�Ų���', 32, 1);
INSERT INTO [line] (code, name, quantity, enable) VALUES ('s-L002', 's-2�Ų���', 27, 1);

INSERT INTO [equipment] (lid, code, name, type, enable) VALUES (1, 's-E0101', 's-1���߽���1��', '�����', 1);
INSERT INTO [equipment] (lid, code, name, type, enable) VALUES (1, 's-E0102', 's-1���߽���2��', '�����', 1);
INSERT INTO [equipment] (lid, code, name, type, enable) VALUES (1, 's-E0103', 's-1���߽���3��', '�����', 1);
INSERT INTO [equipment] (lid, code, name, type, enable) VALUES (1, 's-E0104', 's-1���߽���4��', '�����', 1);
INSERT INTO [equipment] (lid, code, name, type, enable) VALUES (1, 's-E0105', 's-1���߽���5��', '�����', 1);

INSERT INTO [equipment] (lid, code, name, type, enable) VALUES (1, 's-E0106', 's-1���߼���1��', '���ȹ�', 1);
INSERT INTO [equipment] (lid, code, name, type, enable) VALUES (1, 's-E0107', 's-1���߼���2��', '���ȹ�', 1);
INSERT INTO [equipment] (lid, code, name, type, enable) VALUES (1, 's-E0108', 's-1���߼���3��', '���ȹ�', 1);
INSERT INTO [equipment] (lid, code, name, type, enable) VALUES (1, 's-E0109', 's-1���߼���4��', '���ȹ�', 1);
INSERT INTO [equipment] (lid, code, name, type, enable) VALUES (1, 's-E0110', 's-1���߼���5��', '���ȹ�', 1);

INSERT INTO [equipment] (lid, code, name, type, enable) VALUES (2, 's-E0201', 's-2���߽���1��', '�����', 1);
INSERT INTO [equipment] (lid, code, name, type, enable) VALUES (2, 's-E0202', 's-2���߽���2��', '�����', 1);
INSERT INTO [equipment] (lid, code, name, type, enable) VALUES (2, 's-E0203', 's-2���߽���3��', '�����', 1);

INSERT INTO [equipment] (lid, code, name, type, enable) VALUES (2, 's-E0204', 's-2���߼���1��', '���ȹ�', 1);
INSERT INTO [equipment] (lid, code, name, type, enable) VALUES (2, 's-E0205', 's-2���߼���2��', '���ȹ�', 1);
INSERT INTO [equipment] (lid, code, name, type, enable) VALUES (2, 's-E0206', 's-2���߼���3��', '���ȹ�', 1);

INSERT INTO [equipment_gate] (lid, eid, code, name, type, enable) VALUES (1, 1, 's-G0101T1', 's-1���߽���1��1��', '��Ͷ�Ͽ�', 1);
INSERT INTO [equipment_gate] (lid, eid, code, name, type, enable) VALUES (1, 2, 's-G0102T1', 's-1���߽���2��1��', '��Ͷ�Ͽ�', 1);
INSERT INTO [equipment_gate] (lid, eid, code, name, type, enable) VALUES (1, 3, 's-G0103T1', 's-1���߽���3��1��', '��Ͷ�Ͽ�', 1);
INSERT INTO [equipment_gate] (lid, eid, code, name, type, enable) VALUES (1, 4, 's-G0104T1', 's-1���߽���4��1��', '��Ͷ�Ͽ�', 1);
INSERT INTO [equipment_gate] (lid, eid, code, name, type, enable) VALUES (1, 5, 's-G0105T1', 's-1���߽���5��1��', '��Ͷ�Ͽ�', 1);

INSERT INTO [equipment_gate] (lid, eid, code, name, type, enable) VALUES (1, 6, 's-G0106T1', 's-1���߼���1��1��', '��Ͷ�Ͽ�', 1);
INSERT INTO [equipment_gate] (lid, eid, code, name, type, enable) VALUES (1, 7, 's-G0107T1', 's-1���߼���2��1��', '��Ͷ�Ͽ�', 1);
INSERT INTO [equipment_gate] (lid, eid, code, name, type, enable) VALUES (1, 8, 's-G0108T1', 's-1���߼���3��1��', '��Ͷ�Ͽ�', 1);
INSERT INTO [equipment_gate] (lid, eid, code, name, type, enable) VALUES (1, 9, 's-G0109T1', 's-1���߼���4��1��', '��Ͷ�Ͽ�', 1);
INSERT INTO [equipment_gate] (lid, eid, code, name, type, enable) VALUES (1, 10, 's-G0110T1', 's-1���߼���5��1��', '��Ͷ�Ͽ�', 1);

INSERT INTO [equipment_gate] (lid, eid, code, name, type, enable) VALUES (2, 11, 's-G0201T1', 's-2���߽���1��1��', '��Ͷ�Ͽ�', 1);
INSERT INTO [equipment_gate] (lid, eid, code, name, type, enable) VALUES (2, 12, 's-G0202T1', 's-2���߽���2��1��', '��Ͷ�Ͽ�', 1);
INSERT INTO [equipment_gate] (lid, eid, code, name, type, enable) VALUES (2, 13, 's-G0203T1', 's-2���߽���3��1��', '��Ͷ�Ͽ�', 1);

INSERT INTO [equipment_gate] (lid, eid, code, name, type, enable) VALUES (2, 14, 's-G0204T1', 's-2���߼���1��1��', '��Ͷ�Ͽ�', 1);
INSERT INTO [equipment_gate] (lid, eid, code, name, type, enable) VALUES (2, 15, 's-G0205T1', 's-2���߼���2��1��', '��Ͷ�Ͽ�', 1);
INSERT INTO [equipment_gate] (lid, eid, code, name, type, enable) VALUES (2, 16, 's-G0206T1', 's-2���߼���3��1��', '��Ͷ�Ͽ�', 1);

INSERT INTO [weighing_room] (code, name) VALUES ('s-R001', 's-1�����ϼ�');
INSERT INTO [weighing_room] (code, name) VALUES ('s-R002', 's-2�����ϼ�');

INSERT INTO [product] (code, name, unit) VALUES ('s-PNFGY01', 's-ũ���԰', 't');
INSERT INTO [product] (code, name, unit) VALUES ('s-PDFSY01', 's-������Ҷ', 't');
INSERT INTO [product] (code, name, unit) VALUES ('s-PSRC100', 's-ˮ��C100', 't');

INSERT INTO [material] (code, name, type, container, unit, enable) VALUES ('s-FJ01', 's-����Ũ��Һ', 'Һ����', 'Ͱ', 'kg', 1);
INSERT INTO [material] (code, name, type, container, unit, enable) VALUES ('s-XJ01', 's-�㾫A��', '��ĩ��', '��', 'kg', 1);
INSERT INTO [material] (code, name, type, container, unit, enable) VALUES ('s-XL01', 's-����', '������', '��', 'kg', 1);
INSERT INTO [material] (code, name, type, container, unit, enable) VALUES ('s-CY01', 's-��ҶŨ��Һ', 'Һ����', 'Ͱ', 'kg', 1);
INSERT INTO [material] (code, name, type, container, unit, enable) VALUES ('s-CZ01', 's-��֭Ũ��Һ', 'Һ����', 'Ͱ', 'kg', 1);
-- ----------------------------------------------------------------------------
-- ԭ�Ͻ����ϼ�
INSERT INTO [store_material] (mid, rid, original_code, quantity, unit, signed_date, signed_uid) VALUES (1, 1, 's-MO2014060601', 1100, 'kg',
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-06 08:00:00'), 1);
INSERT INTO [store_material] (mid, rid, original_code, quantity, unit, signed_date, signed_uid) VALUES (1, 1, 's-MO2014060701', 1200, 'kg',
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-07 09:00:00'), 1);
INSERT INTO [store_material] (mid, rid, original_code, quantity, unit, signed_date, signed_uid) VALUES (1, 1, 's-MO2014060801', 1300, 'kg',
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-08 10:00:00'), 1);
INSERT INTO [store_material] (mid, rid, original_code, quantity, unit, signed_date, signed_uid) VALUES (1, 1, 's-MO2014060901', 1400, 'kg',
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-09 11:00:00'), 1);
INSERT INTO [store_material] (mid, rid, original_code, quantity, unit, signed_date, signed_uid) VALUES (1, 1, 's-MO2014061001', 1500, 'kg',
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-10 12:00:00'), 1);

INSERT INTO [store_material] (mid, rid, original_code, quantity, unit, signed_date, signed_uid) VALUES (2, 1, 's-MO2014061101', 1500, 'kg',
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-11 12:01:00'), 1);
INSERT INTO [store_material] (mid, rid, original_code, quantity, unit, signed_date, signed_uid) VALUES (2, 1, 's-MO2014061102', 1500, 'kg',
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-11 12:02:00'), 1);

INSERT INTO [store_material] (mid, rid, original_code, quantity, unit, signed_date, signed_uid) VALUES (1, 2, 's-MO2014061103', 1500, 'kg',
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-11 12:03:00'), 1);
INSERT INTO [store_material] (mid, rid, original_code, quantity, unit, signed_date, signed_uid) VALUES (1, 2, 's-MO2014061104', 1500, 'kg',
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-11 12:04:00'), 1);
INSERT INTO [store_material] (mid, rid, original_code, quantity, unit, signed_date, signed_uid) VALUES (2, 2, 's-MO2014061105', 1500, 'kg',
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-11 12:05:00'), 1);
INSERT INTO [store_material] (mid, rid, original_code, quantity, unit, signed_date, signed_uid) VALUES (2, 2, 's-MO2014061106', 1500, 'kg',
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-11 12:06:00'), 1);

--
-- SELECT id, mid, rid, original_code, quantity, unit, signed_date, DATEADD(s, signed_date, '1970-01-01 00:00:00') as signed_date_s, signed_uid FROM [store_material];
--

-- ----------------------------------------------------------------------------
-- �䷽
INSERT INTO [bom] (pid, code, name, quantity, tolerance, unit, enable) VALUES (1, 's-BNFGY01', 'ũ���԰ 1���䷽', 1000, 10, 'kg', 1);
INSERT INTO [bom] (pid, code, name, quantity, tolerance, unit, enable) VALUES (1, 's-BNFGY02', 'ũ���԰ 2���䷽', 1000, 10, 'kg', 1);
INSERT INTO [bom] (pid, code, name, quantity, tolerance, unit, enable) VALUES (2, 's-BDFSY01', '������Ҷ 1���䷽', 1000, 10, 'kg', 1);
INSERT INTO [bom] (pid, code, name, quantity, tolerance, unit, enable) VALUES (2, 's-BDFSY02', '������Ҷ 2���䷽', 1000, 10, 'kg', 1);
INSERT INTO [bom] (pid, code, name, quantity, tolerance, unit, enable) VALUES (3, 's-BSRC101', 'ˮ��C100 1���䷽', 1000, 10, 'kg', 1);
INSERT INTO [bom] (pid, code, name, quantity, tolerance, unit, enable) VALUES (3, 's-BSRC102', 'ˮ��C100 2���䷽', 1000, 10, 'kg', 1);

-- ũ���԰ �䷽��
INSERT INTO [bom_item] (pid, bid, mid, quantity, unit) VALUES (1, 1, 1, 20,  'kg');
INSERT INTO [bom_item] (pid, bid, mid, quantity, unit) VALUES (1, 1, 2, 15,  'kg');
INSERT INTO [bom_item] (pid, bid, mid, quantity, unit) VALUES (1, 1, 3, 1,   'kg');

INSERT INTO [bom_item] (pid, bid, mid, quantity, unit) VALUES (1, 2, 1, 22,  'kg');
INSERT INTO [bom_item] (pid, bid, mid, quantity, unit) VALUES (1, 2, 2, 12,  'kg');
INSERT INTO [bom_item] (pid, bid, mid, quantity, unit) VALUES (1, 2, 3, 1.5, 'kg');

-- ������Ҷ �䷽��
INSERT INTO [bom_item] (pid, bid, mid, quantity, unit) VALUES (2, 3, 4, 30,  'kg');
INSERT INTO [bom_item] (pid, bid, mid, quantity, unit) VALUES (2, 3, 2, 60,  'kg');
INSERT INTO [bom_item] (pid, bid, mid, quantity, unit) VALUES (2, 3, 3, 1.5, 'kg');

INSERT INTO [bom_item] (pid, bid, mid, quantity, unit) VALUES (2, 4, 4, 32,  'kg');
INSERT INTO [bom_item] (pid, bid, mid, quantity, unit) VALUES (2, 4, 2, 80,  'kg');
INSERT INTO [bom_item] (pid, bid, mid, quantity, unit) VALUES (2, 4, 3, 1.5, 'kg');

-- ˮ��C100 �䷽��
INSERT INTO [bom_item] (pid, bid, mid, quantity, unit) VALUES (3, 3, 5, 52,  'kg');
INSERT INTO [bom_item] (pid, bid, mid, quantity, unit) VALUES (3, 3, 2, 16,  'kg');
INSERT INTO [bom_item] (pid, bid, mid, quantity, unit) VALUES (3, 3, 3, 1.5, 'kg');

INSERT INTO [bom_item] (pid, bid, mid, quantity, unit) VALUES (3, 4, 5, 56,  'kg');
INSERT INTO [bom_item] (pid, bid, mid, quantity, unit) VALUES (3, 4, 2, 16,  'kg');
INSERT INTO [bom_item] (pid, bid, mid, quantity, unit) VALUES (3, 4, 3, 1,   'kg');

-- ----------------------------------------------------------------------------
--  ����

INSERT INTO [process_flow] (pid, lid, bid, code, name, type, enable) VALUES (1, 1, 1, 'F1NFGY0101', 'ũ���԰ �䷽1 ����1',  1, 1); -- ����Ͷ��
INSERT INTO [process_flow] (pid, lid, bid, code, name, type, enable) VALUES (1, 1, 1, 'F1NFGY0102', 'ũ���԰ �䷽1 ����2',  2, 1); -- ����Ͷ��
INSERT INTO [process_flow] (pid, lid, bid, code, name, type, enable) VALUES (1, 1, 2, 'F1NFGY0201', 'ũ���԰ �䷽2 ����1',  1, 1); -- ����Ͷ��
INSERT INTO [process_flow] (pid, lid, bid, code, name, type, enable) VALUES (1, 1, 2, 'F1NFGY0202', 'ũ���԰ �䷽2 ����2',  2, 1); -- ����Ͷ��

INSERT INTO [process_flow] (pid, lid, bid, code, name, type, enable) VALUES (2, 2, 3, 'F2DFSY0101', '������Ҷ �䷽1 ����1',  1, 1); -- ����Ͷ��
INSERT INTO [process_flow] (pid, lid, bid, code, name, type, enable) VALUES (2, 2, 3, 'F2DFSY0102', '������Ҷ �䷽1 ����2',  2, 1); -- ����Ͷ��
INSERT INTO [process_flow] (pid, lid, bid, code, name, type, enable) VALUES (2, 2, 4, 'F2DFSY0201', '������Ҷ �䷽2 ����1',  1, 1); -- ����Ͷ��
INSERT INTO [process_flow] (pid, lid, bid, code, name, type, enable) VALUES (2, 2, 4, 'F2DFSY0202', '������Ҷ �䷽2 ����2',  2, 1); -- ����Ͷ��

INSERT INTO [process_flow] (pid, lid, bid, code, name, type, enable) VALUES (3, 1, 5, 'F1SRC10101', 'ˮ��C100 �䷽1 ����1',  1, 1); -- ����Ͷ��
INSERT INTO [process_flow] (pid, lid, bid, code, name, type, enable) VALUES (3, 1, 5, 'F1SRC10102', 'ˮ��C100 �䷽1 ����2',  2, 1); -- ����Ͷ��
INSERT INTO [process_flow] (pid, lid, bid, code, name, type, enable) VALUES (3, 1, 6, 'F1SRC10201', 'ˮ��C100 �䷽2 ����1',  1, 1); -- ����Ͷ��
INSERT INTO [process_flow] (pid, lid, bid, code, name, type, enable) VALUES (3, 1, 6, 'F1SRC10202', 'ˮ��C100 �䷽2 ����2',  2, 1); -- ����Ͷ��

-- ũ���԰ ������ -- ����Ͷ��
INSERT INTO [process_flow_item] (fid, pid, lid, bid, mid, eqpt_type, gate_type, sequence, interval, enable) VALUES (1, 1, 1, 1, 1, '�����', '��Ͷ�Ͽ�', 0, 0, 1); 
INSERT INTO [process_flow_item] (fid, pid, lid, bid, mid, eqpt_type, gate_type, sequence, interval, enable) VALUES (1, 1, 1, 1, 2, '�����', '��Ͷ�Ͽ�', 0, 0, 1);
INSERT INTO [process_flow_item] (fid, pid, lid, bid, mid, eqpt_type, gate_type, sequence, interval, enable) VALUES (1, 1, 1, 1, 3, '�����', '��Ͷ�Ͽ�', 0, 0, 1);

-- ũ���԰ ������ -- ����Ͷ��
INSERT INTO [process_flow_item] (fid, pid, lid, bid, mid, eqpt_type, gate_type, sequence, interval, enable) VALUES (2, 1, 1, 2, 1, '���ȹ�', '��Ͷ�Ͽ�', 1, 10, 1); 
INSERT INTO [process_flow_item] (fid, pid, lid, bid, mid, eqpt_type, gate_type, sequence, interval, enable) VALUES (2, 1, 1, 2, 2, '���ȹ�', '��Ͷ�Ͽ�', 2, 30, 1);
INSERT INTO [process_flow_item] (fid, pid, lid, bid, mid, eqpt_type, gate_type, sequence, interval, enable) VALUES (2, 1, 1, 2, 3, '���ȹ�', '��Ͷ�Ͽ�', 3, 20, 1);

-- ������Ҷ ������ -- ����Ͷ��
INSERT INTO [process_flow_item] (fid, pid, lid, bid, mid, eqpt_type, gate_type, sequence, interval, enable) VALUES (3, 2, 2, 3, 1, '�����', '��Ͷ�Ͽ�', 0, 0, 1); 
INSERT INTO [process_flow_item] (fid, pid, lid, bid, mid, eqpt_type, gate_type, sequence, interval, enable) VALUES (3, 2, 2, 3, 2, '�����', '��Ͷ�Ͽ�', 0, 0, 1);
INSERT INTO [process_flow_item] (fid, pid, lid, bid, mid, eqpt_type, gate_type, sequence, interval, enable) VALUES (3, 2, 2, 3, 3, '�����', '��Ͷ�Ͽ�', 0, 0, 1);

-- ������Ҷ ������ -- ����Ͷ��
INSERT INTO [process_flow_item] (fid, pid, lid, bid, mid, eqpt_type, gate_type, sequence, interval, enable) VALUES (4, 2, 2, 4, 1, '�����', '��Ͷ�Ͽ�', 1, 30, 1); 
INSERT INTO [process_flow_item] (fid, pid, lid, bid, mid, eqpt_type, gate_type, sequence, interval, enable) VALUES (4, 2, 2, 4, 2, '�����', '��Ͷ�Ͽ�', 2, 30, 1);
INSERT INTO [process_flow_item] (fid, pid, lid, bid, mid, eqpt_type, gate_type, sequence, interval, enable) VALUES (4, 2, 2, 4, 3, '�����', '��Ͷ�Ͽ�', 3, 60, 1);

-- ˮ��C100 ������ -- ����Ͷ��
INSERT INTO [process_flow_item] (fid, pid, lid, bid, mid, eqpt_type, gate_type, sequence, interval, enable) VALUES (5, 3, 1, 5, 1, '�����', '��Ͷ�Ͽ�', 0, 0, 1); 
INSERT INTO [process_flow_item] (fid, pid, lid, bid, mid, eqpt_type, gate_type, sequence, interval, enable) VALUES (5, 3, 1, 5, 2, '�����', '��Ͷ�Ͽ�', 0, 0, 1);
INSERT INTO [process_flow_item] (fid, pid, lid, bid, mid, eqpt_type, gate_type, sequence, interval, enable) VALUES (5, 3, 1, 5, 3, '�����', '��Ͷ�Ͽ�', 0, 0, 1);

-- ˮ��C100 ������ -- ����Ͷ��
INSERT INTO [process_flow_item] (fid, pid, lid, bid, mid, eqpt_type, gate_type, sequence, interval, enable) VALUES (6, 3, 1, 6, 1, '�����', '��Ͷ�Ͽ�', 1, 30, 1); 
INSERT INTO [process_flow_item] (fid, pid, lid, bid, mid, eqpt_type, gate_type, sequence, interval, enable) VALUES (6, 3, 1, 6, 2, '�����', '��Ͷ�Ͽ�', 2, 30, 1);
INSERT INTO [process_flow_item] (fid, pid, lid, bid, mid, eqpt_type, gate_type, sequence, interval, enable) VALUES (6, 3, 1, 6, 3, '�����', '��Ͷ�Ͽ�', 3, 60, 1);


-- �ƻ�
INSERT INTO [schedule] (fid, pid, lid, bid, code, quantity, unit, schd_sdate, schd_edate, schd_time, appr_time, schd_uid, appr_uid) VALUES (1, 1, 1, 1, 'S140606001', 3200, 't',
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-06 08:30:00'),
DATEDIFF(s, '1970-01-01 00:00:00', '2014-08-05 17:30:00'),
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-06 08:00:00'),
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-06 08:20:00'),
2, 1);

--
SELECT id, fid, pid, lid, bid, code, quantity, unit, schd_sdate, schd_edate, schd_time, appr_time, schd_uid, appr_uid,
	DATEADD(s, schd_sdate, '1970-01-01 00:00:00') as schd_sdate_s,
	DATEADD(s, schd_edate, '1970-01-01 00:00:00') as schd_edate_s,
	DATEADD(s, schd_time, '1970-01-01 00:00:00') as schd_time_s,
	DATEADD(s, appr_time, '1970-01-01 00:00:00') as appr_time_s
FROM [schedule];
--


-- ����
INSERT INTO [workorder] (sid, fid, pid, lid, bid, code, sequence, quantity, unit, work_sdate, work_edate, status, owner_uid, weighing_uid, operator_uid) VALUES (1, 1, 1, 1, 1, 'W140606001', 1, 32, 't', 
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-06 08:00:00'),
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-06 17:30:00'), 9, 1, 1, 2); -- ���
INSERT INTO [workorder] (sid, fid, pid, lid, bid, code, sequence, quantity, unit, work_sdate, work_edate, status, owner_uid, weighing_uid, operator_uid) VALUES (1, 1, 1, 1, 1, 'W140607001', 2, 32, 't',
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-07 08:00:00'),
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-07 17:30:00'), 5, 1, 1, 2); -- ��Ͷ
INSERT INTO [workorder] (sid, fid, pid, lid, bid, code, sequence, quantity, unit, work_sdate, work_edate, status, owner_uid, weighing_uid, operator_uid) VALUES (1, 1, 1, 1, 1, 'W140608001', 3, 32, 't',
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-08 08:00:00'),
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-08 17:30:00'), 4, 1, 1, 2); -- ����
INSERT INTO [workorder] (sid, fid, pid, lid, bid, code, sequence, quantity, unit, work_sdate, work_edate, status, owner_uid, weighing_uid, operator_uid) VALUES (1, 1, 1, 1, 1, 'W140609001', 1, 32, 't',
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-09 08:00:00'),
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-09 17:30:00'), 3, 1, 1, 2); -- �Ѹ�
INSERT INTO [workorder] (sid, fid, pid, lid, bid, code, sequence, quantity, unit, work_sdate, work_edate, status, owner_uid, weighing_uid, operator_uid) VALUES (1, 1, 1, 1, 2, 'W140610001', 1, 32, 't',
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-10 08:00:00'),
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-10 17:30:00'), 2, 1, 1, 2); -- ����
INSERT INTO [workorder] (sid, fid, pid, lid, bid, code, sequence, quantity, unit, work_sdate, work_edate, status, owner_uid, weighing_uid, operator_uid) VALUES (1, 1, 1, 1, 1, 'W140611001', 1, 32, 't',
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-11 08:00:00'),
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-11 17:30:00'), 1, 1, 1, 2); -- ����
INSERT INTO [workorder] (sid, fid, pid, lid, bid, code, sequence, quantity, unit, work_sdate, work_edate, status, owner_uid, weighing_uid, operator_uid) VALUES (1, 1, 1, 1, 1, 'W140612001', 1, 32, 't',
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-12 08:00:00'),
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-12 17:30:00'), 0, 1, 1, 2); -- δִ��
--
SELECT sid, fid, pid, lid, bid, code, sequence, quantity, unit, work_sdate, work_edate, status, owner_uid, weighing_uid, operator_uid,
	DATEADD(s, work_sdate, '1970-01-01 00:00:00') as work_sdate_s,
	DATEADD(s, work_edate, '1970-01-01 00:00:00') as work_edate_s
FROM [workorder];
--


-- ����ԭ��
-- ����1���
INSERT INTO [workorder_material] (wid, mid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (1, 1, 32*20+3,  32*20+0.5, 32*20, 1, 3, 'kg', 5);
INSERT INTO [workorder_material] (wid, mid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (1, 2, 32*15+3,  32*15+0.5, 32*15, 1, 3, 'kg', 5);
INSERT INTO [workorder_material] (wid, mid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (1, 3, 32*1+1,   32*1+0.2,  32*1,  1, 3, 'kg', 5);

-- ����2��Ͷ (�ͻ���û��������ɱ�ǣ�ԭ��Ϊ��Ͷ�Ϻ���Ҫ�����մ���ʱ��)
INSERT INTO [workorder_material] (wid, mid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (2, 1, 32*20+3,  32*20+0.5, 32*20, 1, 3, 'kg', 5);
INSERT INTO [workorder_material] (wid, mid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (2, 2, 32*15+3,  32*15+0.5, 32*15, 1, 3, 'kg', 5);
INSERT INTO [workorder_material] (wid, mid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (2, 3, 32*1+1,   32*1+0.2,  32*1,  1, 3, 'kg', 5);

-- ����3���� (���й���ԭ��status>=4, ���Ҵ�������һ��ԭ��status=4)
INSERT INTO [workorder_material] (wid, mid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (3, 1, 32*20+3,  32*20+0.5, 32*20, 1, 3, 'kg', 4);
INSERT INTO [workorder_material] (wid, mid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (3, 2, 32*15+3,  32*15+0.5, 32*15, 1, 3, 'kg', 4);
INSERT INTO [workorder_material] (wid, mid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (3, 3, 32*1+1,   32*1+0.2,  32*1,  1, 3, 'kg', 5); -- ��ԭ��ȫ��Ͷ�꣬��������ԭ��û��ȫ��Ͷ��

-- ����4�Ѹ���
INSERT INTO [workorder_material] (wid, mid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (4, 1, 32*20*1.0009+4,  32*22*1.0009,	  32*22,  1, 3, 'kg', 3);
INSERT INTO [workorder_material] (wid, mid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (4, 2, 32*15*1.0009+4,  32*12*1.0009,	  32*12,  1, 3, 'kg', 3);
INSERT INTO [workorder_material] (wid, mid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (4, 3, 32*1.5*1.0009+1,   32*1.5*1.0009,  32*1.5, 1, 3, 'kg', 3);

-- ����5����
INSERT INTO [workorder_material] (wid, mid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (5, 1, 32*20+4,  32*20+0.5, 32*20, 1, 3, 'kg', 2);
INSERT INTO [workorder_material] (wid, mid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (5, 2, 32*15+4,  32*15+0.5, 32*15, 1, 3, 'kg', 2);
INSERT INTO [workorder_material] (wid, mid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (5, 3, 32*1+1,   32*1+0.2,  32*1,  1, 3, 'kg', 2);

-- ����6����
INSERT INTO [workorder_material] (wid, mid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (6, 1, 32*20*1.0009+4,  32*20*1.0009, 32*20, 1, 0, 'kg', 1);
INSERT INTO [workorder_material] (wid, mid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (6, 2, 32*15*1.0009+4,  32*15*1.0009, 32*15, 1, 0, 'kg', 1);
INSERT INTO [workorder_material] (wid, mid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (6, 3, 32*1*1.0009+1,   32*1*1.0009,  32*1,  1, 0, 'kg', 1);

-- ����7δִ��
INSERT INTO [workorder_material] (wid, mid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (7, 1, 32*20+4,  32*20+0.5, 32*20, 1, 0, 'kg', 0);
INSERT INTO [workorder_material] (wid, mid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (7, 2, 32*15+4,  32*15+0.5, 32*15, 1, 0, 'kg', 0);
INSERT INTO [workorder_material] (wid, mid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (7, 3, 32*1+1,   32*1+0.2,  32*1,  1, 0, 'kg', 0);

-- ����ԭ��Ͱ
-- ����1��� s-����Ũ��Һ 32*20 = 640 kg, ÿ��Ͷ�Ͽ�Ͷ2Ͱ��ÿͰ80kg���ҡ�
INSERT INTO [workorder_container] (wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (1, 1, 1, 1,		1, 1, 1, 1,
1, 80.1+3, 80.1, 'kg', 5);
INSERT INTO [workorder_container] (wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (1, 1, 1, 1,		1, 1, 1, 1,
2, 80+3, 80.0, 'kg', 5);
INSERT INTO [workorder_container] (wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (1, 1, 1, 1,		1, 1, 2, 2,
3, 80.1+3, 80.1, 'kg', 5);
INSERT INTO [workorder_container] (wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (1, 1, 1, 1,		1, 1, 2, 2,
4, 80.3+3, 80.3, 'kg', 5);
INSERT INTO [workorder_container] (wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (1, 1, 1, 1,		1, 1, 3, 3,
5, 80.1+3, 80.1, 'kg', 5);
INSERT INTO [workorder_container] (wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (1, 1, 1, 1,		1, 1, 3, 3,
6, 80+3, 80.0, 'kg', 5);
INSERT INTO [workorder_container] (wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (1, 1, 1, 1,		1, 1, 4, 4,
7, 80.1+3, 80.1, 'kg', 5);
INSERT INTO [workorder_container] (wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (1, 1, 1, 1,		1, 1, 4, 4,
8, 80.3+3, 80.3, 'kg', 5);

-- ����1��� s-�㾫A�� 32*15 = 480 kg, ÿ��Ͷ�Ͽ�Ͷ1Ͱ��ÿͰ120kg���ҡ�
INSERT INTO [workorder_container] (wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (1, 2, 1, 1,		1, 1, 1, 1,
1, 120.0+5, 120.0, 'kg', 5);
INSERT INTO [workorder_container] (wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (1, 2, 1, 1,		1, 1, 2, 2,
2, 120.3+5, 120.3, 'kg', 5);
INSERT INTO [workorder_container] (wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (1, 2, 1, 1,		1, 1, 3, 3,
3, 120.8+5, 119.8, 'kg', 5);
INSERT INTO [workorder_container] (wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (1, 2, 1, 1,		1, 1, 4, 4,
4, 120.1+5, 120.1, 'kg', 5);

-- ����1��� s-���� 32*1 = 32 kg, 2��Ͷ�Ͽ�Ͷ1��ÿ��16kg���ҡ�
INSERT INTO [workorder_container] (wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (1, 3, 1, 1,		1, 1, 1, 1,
1, 16.3+0.3, 16.3, 'kg', 5);
INSERT INTO [workorder_container] (wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (1, 3, 1, 1,		1, 1, 3, 3,
2, 16.1+0.3, 16.1, 'kg', 5);


-- ����ԭ��Ͱ
-- ����4�Ѹ��� s-����Ũ��Һ 32*22 = 704 kg, ÿ��Ͷ�Ͽ�Ͷ1Ͱ��ÿͰ176kg���ҡ�
INSERT INTO [workorder_container] (wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (4, 1, 1, 2,		1, 1, 1, 1,
1, 176+3, 176, 'kg', 3);
INSERT INTO [workorder_container] (wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (4, 1, 1, 2,		1, 1, 2, 2,
2, 176+3, 176, 'kg', 3);
INSERT INTO [workorder_container] (wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (4, 1, 1, 2,		1, 1, 3, 3,
3, 176+3, 176, 'kg', 3);
INSERT INTO [workorder_container] (wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (4, 1, 1, 2,		1, 1, 4, 4,
4, 176+3, 176, 'kg', 3);

-- ����4�Ѹ��� s-�㾫A�� 32*12 = 384 kg, ÿ��Ͷ�Ͽ�Ͷ1��ÿ��96kg���ҡ�
INSERT INTO [workorder_container] (wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (4, 2, 1, 2,		1, 1, 1, 1,
1, 96.0+5, 96.0, 'kg', 3);
INSERT INTO [workorder_container] (wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (4, 2, 1, 2,		1, 1, 2, 2,
2, 96.3+5, 96.3, 'kg', 3);
INSERT INTO [workorder_container] (wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (4, 2, 1, 2,		1, 1, 3, 3,
3, 96.8+5, 96.8, 'kg', 3);
INSERT INTO [workorder_container] (wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (4, 2, 1, 2,		1, 1, 4, 4,
4, 96.1+5, 96.1, 'kg', 3);

-- ����4�Ѹ��� s-���� 32*1.5 = 48 kg, ÿ��Ͷ�Ͽ�Ͷ1��ÿ��12kg���ҡ�
INSERT INTO [workorder_container] (wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (4, 3, 1, 2,		1, 1, 1, 1,
1, 12.3+0.3, 12.3, 'kg', 3);
INSERT INTO [workorder_container] (wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (4, 3, 1, 2,		1, 1, 2, 2,
2, 12.1+0.3, 12.1, 'kg', 3);
INSERT INTO [workorder_container] (wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (4, 3, 1, 2,		1, 1, 3, 3,
3, 12.3+0.3, 12.3, 'kg', 3);
INSERT INTO [workorder_container] (wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (4, 3, 1, 2,		1, 1, 4, 4,
4, 12.1+0.3, 12.1, 'kg', 3);

-- ----------------------------------------------------------------------------
-- End of Initiailize Data
-- ----------------------------------------------------------------------------
