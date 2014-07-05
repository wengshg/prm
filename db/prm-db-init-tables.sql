-- ----------------------------------------------------------------------------
-- PRM MS SERVER SQL SCRIPT
-- V1.0 2014-6-4
-- V1.1 2014-6-11
-- V1.2 2014-6-12
-- V1.2.1 2014-6-15, add workorder_material.tolerance
-- V1.2.2 2014-6-15, add intialized data of weighing_room, workorder_container
-- V1.2.3 2014-6-15, remove NOT NULL for workorder_log.uid
-- V1.2.4 2014-6-20, split to drop tables file & init talbes file
-- V1.3 2014-6-27, add workorder.eid, process_flow.type change to tinyint.

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
    unit varchar(8),
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

CREATE TABLE container(
    id int NOT NULL PRIMARY KEY IDENTITY(1,1),
    code varchar(32) NOT NULL unique,
    name varchar(32) NOT NULL unique,
    type varchar(32),
    quantity float,
    unit varchar(8),
    enable tinyint
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
    tolerance float,
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
    type tinyint,
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
    lid int NOT NULL constraint fk_schedule_lid foreign key references line(id),
    pid int NOT NULL constraint fk_schedule_pid foreign key references product(id),
    bid int NOT NULL constraint fk_schedule_bid foreign key references bom(id),
    fid int NOT NULL constraint fk_schedule_fid foreign key references process_flow(id),
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
    sequence smallint,
    sid int NOT NULL constraint fk_workorder_sid foreign key references schedule(id),
    lid int NOT NULL constraint fk_workorder_lid foreign key references line(id),
    eid int NOT NULL constraint fk_workorder_eid foreign key references equipment(id), -- 对应一个桶（罐体）
    pid int NOT NULL constraint fk_workorder_pid foreign key references product(id),
    bid int NOT NULL constraint fk_workorder_bid foreign key references bom(id),
    fid int NOT NULL constraint fk_workorder_fid foreign key references process_flow(id),
    code varchar(32) NOT NULL unique,
    quantity float,
    unit varchar(8),
    work_sdate bigint, 
    work_edate bigint, 
    status tinyint,
    owner_uid int NOT NULL constraint fk_workorder_owner_uid foreign key references [user](id),
    weighing_uid int constraint fk_workorder_weighing_uid foreign key references [user](id),
    operator_uid int constraint fk_workorder_operator_uid foreign key references [user](id)
);

CREATE TABLE workorder_material (
    id int NOT NULL PRIMARY KEY IDENTITY(1,1),
    sid int NOT NULL constraint fk_workorder_material_sid foreign key references schedule(id),
    wid int NOT NULL constraint fk_workorder_material_bid foreign key references workorder(id),
    mid int NOT NULL constraint fk_workorder_material_mid foreign key references material(id),
    lid int NOT NULL constraint fk_workorder_material_lid foreign key references line(id),
    eid int NOT NULL constraint fk_workorder_material_eid foreign key references equipment(id),
    pid int NOT NULL constraint fk_workorder_material_pid foreign key references product(id),
    actl_total float,
    actl_quantity float,
    quantity float,
    tolerance float,
    container_qty int,
    unit varchar(8),
    replenish tinyint,
    status tinyint
);

CREATE TABLE workorder_container (
    id int NOT NULL PRIMARY KEY IDENTITY(1,1),
    sid int NOT NULL constraint fk_workorder_container_sid foreign key references schedule(id),
    wid int NOT NULL constraint fk_workorder_container_wid foreign key references workorder(id),
    mid int NOT NULL constraint fk_workorder_container_mid foreign key references material(id),
    pid int NOT NULL constraint fk_workorder_container_pid foreign key references product(id),
    bid int NOT NULL constraint fk_workorder_container_bid foreign key references bom(id),
    fid int NOT NULL constraint fk_workorder_container_fid foreign key references process_flow(id),
    lid int NOT NULL constraint fk_workorder_container_lid foreign key references line(id),
    eid int NOT NULL constraint fk_workorder_container_eid foreign key references equipment(id),
    gid int NOT NULL constraint fk_workorder_container_gid foreign key references equipment_gate(id),
    sequence smallint,
    total float,
    quantity float,
    unit varchar(8),
    replenished tinyint,
    status tinyint
);

CREATE TABLE workorder_log (
    id int NOT NULL PRIMARY KEY IDENTITY(1,1),
    sid int NOT NULL constraint fk_workorder_log_sid foreign key references schedule(id),
    wid int NOT NULL constraint fk_workorder_log_bid foreign key references workorder(id),
    mid int,
    pid int NOT NULL constraint fk_workorder_log_pid foreign key references product(id),
    lid int NOT NULL constraint fk_workorder_log_lid foreign key references line(id),
    eid int,
    gid int,
    sequence smallint,
    uid int,
    status tinyint,
    created_time bigint,
    created_uid int NOT NULL constraint fk_workorder_log_created_uid foreign key references [user](id)
);

CREATE TABLE store_material (
    id int NOT NULL PRIMARY KEY IDENTITY(1,1),
    rid int NOT NULL constraint fk_store_material_rid foreign key references weighing_room(id),
    mid int NOT NULL constraint fk_store_material_mid foreign key references material(id),
    sid int,
    wid int,
    original_code varchar(64),
    quantity float,
    unit varchar(8),
    signed_date bigint,
    signed_uid int NOT NULL constraint fk_store_material_signed_Uid foreign key references [user](id)
);

CREATE TABLE store_requisition (
    id int NOT NULL PRIMARY KEY IDENTITY(1,1),
    sid int NOT NULL constraint fk_store_requisition_rid foreign key references schedule(id),
    wid int constraint fk_store_requisition_wid foreign key references workorder(id),
    code varchar(32) NOT NULL unique,
    created_date bigint,
    created_uid int NOT NULL constraint fk_store_requisition_created_uid foreign key references [user](id),
    signed_date bigint,
    signed_uid int constraint fk_store_requisition_signed_uid foreign key references [user](id)
);

CREATE TABLE store_requisition_item (
    id int NOT NULL PRIMARY KEY IDENTITY(1,1),
    qid int NOT NULL constraint fk_store_requisition_item_qid foreign key references store_requisition(id),
    sid int NOT NULL constraint fk_store_requisition_item_sid foreign key references schedule(id),
    wid int constraint fk_requisition_item_wid foreign key references workorder(id),
    mid int NOT NULL constraint fk_store_requisition_item_mid foreign key references material(id),
    quantity float,
    unit varchar(8)
);

-- ----------------------------------------------------------------------------
-- End of Initiailized Database Tables
-- ----------------------------------------------------------------------------

-- ----------------------------------------------------------------------------
-- Initiailize Data
-- ----------------------------------------------------------------------------

INSERT INTO [user] (username, passwd, name, dept, role, memo, enable) VALUES ('s-test1', 'spass', '张主任', '办公室', '车间主任', 'memo', 1);
INSERT INTO [user] (username, passwd, name, dept, role, memo, enable) VALUES ('s-test2', 'spass', '李审核', '办公室', '车间主任', 'memo', 1);
INSERT INTO [user] (username, passwd, name, dept, role, memo, enable) VALUES ('s-test3', 'spass', '张三', '配料部', '配料员', 'memo', 1);
INSERT INTO [user] (username, passwd, name, dept, role, memo, enable) VALUES ('s-test4', 'spass', '李四', '品保部', '品保员', 'memo', 1);
INSERT INTO [user] (username, passwd, name, dept, role, memo, enable) VALUES ('s-test5', 'spass', '王五', '领料部', '领料员', 'memo', 1);

INSERT INTO [line] (code, name, quantity, unit, enable) VALUES ('s-L001', 's-1号产线', 32000, 'kl', 1);
INSERT INTO [line] (code, name, quantity, unit, enable) VALUES ('s-L002', 's-2号产线', 27000, 'kl', 1);

INSERT INTO [equipment] (lid, code, name, type, enable) VALUES (1, 's-E0101', 's-1号线搅拌1罐', '搅拌罐', 1);
INSERT INTO [equipment] (lid, code, name, type, enable) VALUES (1, 's-E0102', 's-1号线搅拌2罐', '搅拌罐', 1);
INSERT INTO [equipment] (lid, code, name, type, enable) VALUES (1, 's-E0103', 's-1号线搅拌3罐', '搅拌罐', 1);
INSERT INTO [equipment] (lid, code, name, type, enable) VALUES (1, 's-E0104', 's-1号线搅拌4罐', '搅拌罐', 1);
INSERT INTO [equipment] (lid, code, name, type, enable) VALUES (1, 's-E0105', 's-1号线搅拌5罐', '搅拌罐', 1);

INSERT INTO [equipment] (lid, code, name, type, enable) VALUES (1, 's-E0106', 's-1号线加热1罐', '加热罐', 1);
INSERT INTO [equipment] (lid, code, name, type, enable) VALUES (1, 's-E0107', 's-1号线加热2罐', '加热罐', 1);
INSERT INTO [equipment] (lid, code, name, type, enable) VALUES (1, 's-E0108', 's-1号线加热3罐', '加热罐', 1);
INSERT INTO [equipment] (lid, code, name, type, enable) VALUES (1, 's-E0109', 's-1号线加热4罐', '加热罐', 1);
INSERT INTO [equipment] (lid, code, name, type, enable) VALUES (1, 's-E0110', 's-1号线加热5罐', '加热罐', 1);

INSERT INTO [equipment] (lid, code, name, type, enable) VALUES (2, 's-E0201', 's-2号线搅拌1罐', '搅拌罐', 1);
INSERT INTO [equipment] (lid, code, name, type, enable) VALUES (2, 's-E0202', 's-2号线搅拌2罐', '搅拌罐', 1);
INSERT INTO [equipment] (lid, code, name, type, enable) VALUES (2, 's-E0203', 's-2号线搅拌3罐', '搅拌罐', 1);

INSERT INTO [equipment] (lid, code, name, type, enable) VALUES (2, 's-E0204', 's-2号线加热1罐', '加热罐', 1);
INSERT INTO [equipment] (lid, code, name, type, enable) VALUES (2, 's-E0205', 's-2号线加热2罐', '加热罐', 1);
INSERT INTO [equipment] (lid, code, name, type, enable) VALUES (2, 's-E0206', 's-2号线加热3罐', '加热罐', 1);

INSERT INTO [equipment_gate] (lid, eid, code, name, type, enable) VALUES (1, 1, 's-G0101T1', 's-1号线搅拌1罐1口', '大投料口', 1);
INSERT INTO [equipment_gate] (lid, eid, code, name, type, enable) VALUES (1, 2, 's-G0102T1', 's-1号线搅拌2罐1口', '大投料口', 1);
INSERT INTO [equipment_gate] (lid, eid, code, name, type, enable) VALUES (1, 3, 's-G0103T1', 's-1号线搅拌3罐1口', '大投料口', 1);
INSERT INTO [equipment_gate] (lid, eid, code, name, type, enable) VALUES (1, 4, 's-G0104T1', 's-1号线搅拌4罐1口', '大投料口', 1);
INSERT INTO [equipment_gate] (lid, eid, code, name, type, enable) VALUES (1, 5, 's-G0105T1', 's-1号线搅拌5罐1口', '大投料口', 1);

INSERT INTO [equipment_gate] (lid, eid, code, name, type, enable) VALUES (1, 6, 's-G0106T1', 's-1号线加热1罐1口', '大投料口', 1);
INSERT INTO [equipment_gate] (lid, eid, code, name, type, enable) VALUES (1, 7, 's-G0107T1', 's-1号线加热2罐1口', '大投料口', 1);
INSERT INTO [equipment_gate] (lid, eid, code, name, type, enable) VALUES (1, 8, 's-G0108T1', 's-1号线加热3罐1口', '大投料口', 1);
INSERT INTO [equipment_gate] (lid, eid, code, name, type, enable) VALUES (1, 9, 's-G0109T1', 's-1号线加热4罐1口', '大投料口', 1);
INSERT INTO [equipment_gate] (lid, eid, code, name, type, enable) VALUES (1, 10, 's-G0110T1', 's-1号线加热5罐1口', '大投料口', 1);

INSERT INTO [equipment_gate] (lid, eid, code, name, type, enable) VALUES (2, 11, 's-G0201T1', 's-2号线搅拌1罐1口', '大投料口', 1);
INSERT INTO [equipment_gate] (lid, eid, code, name, type, enable) VALUES (2, 12, 's-G0202T1', 's-2号线搅拌2罐1口', '大投料口', 1);
INSERT INTO [equipment_gate] (lid, eid, code, name, type, enable) VALUES (2, 13, 's-G0203T1', 's-2号线搅拌3罐1口', '大投料口', 1);

INSERT INTO [equipment_gate] (lid, eid, code, name, type, enable) VALUES (2, 14, 's-G0204T1', 's-2号线加热1罐1口', '大投料口', 1);
INSERT INTO [equipment_gate] (lid, eid, code, name, type, enable) VALUES (2, 15, 's-G0205T1', 's-2号线加热2罐1口', '大投料口', 1);
INSERT INTO [equipment_gate] (lid, eid, code, name, type, enable) VALUES (2, 16, 's-G0206T1', 's-2号线加热3罐1口', '大投料口', 1);

INSERT INTO [container] (code, name, type, quantity, unit, enable) VALUES ('s-C001', '5Kg液料桶', '液料桶', 5, 'kg', 1);
INSERT INTO [container] (code, name, type, quantity, unit, enable) VALUES ('s-C002', '2g塑料袋', '塑料袋', 0.002, 'kg', 1);

INSERT INTO [weighing_room] (code, name) VALUES ('s-R001', 's-1号配料间');
INSERT INTO [weighing_room] (code, name) VALUES ('s-R002', 's-2号配料间');

INSERT INTO [product] (code, name, unit) VALUES ('s-PNFGY01', 's-农夫果园', 't');
INSERT INTO [product] (code, name, unit) VALUES ('s-PDFSY01', 's-东方树叶', 't');
INSERT INTO [product] (code, name, unit) VALUES ('s-PSRC100', 's-水溶C100', 't');

INSERT INTO [material] (code, name, type, container, unit, enable) VALUES ('s-FJ01', 's-番茄浓缩液', '液体料', '桶', 'kg', 1);
INSERT INTO [material] (code, name, type, container, unit, enable) VALUES ('s-XJ01', 's-香精A号', '粉末料', '包', 'kg', 1);
INSERT INTO [material] (code, name, type, container, unit, enable) VALUES ('s-XL01', 's-香料', '固体料', '包', 'kg', 1);
INSERT INTO [material] (code, name, type, container, unit, enable) VALUES ('s-CY01', 's-茶叶浓缩液', '液体料', '桶', 'kg', 1);
INSERT INTO [material] (code, name, type, container, unit, enable) VALUES ('s-CZ01', 's-橙汁浓缩液', '液体料', '桶', 'kg', 1);
-- ----------------------------------------------------------------------------
-- 原料进配料间
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
-- 配方
INSERT INTO [bom] (pid, code, name, quantity, tolerance, unit, enable) VALUES (1, 's-BNFGY01', '农夫果园 1号配方', 1000, 10, 'kg', 1);
INSERT INTO [bom] (pid, code, name, quantity, tolerance, unit, enable) VALUES (1, 's-BNFGY02', '农夫果园 2号配方', 1000, 10, 'kg', 1);
INSERT INTO [bom] (pid, code, name, quantity, tolerance, unit, enable) VALUES (2, 's-BDFSY01', '东方树叶 1号配方', 1000, 10, 'kg', 1);
INSERT INTO [bom] (pid, code, name, quantity, tolerance, unit, enable) VALUES (2, 's-BDFSY02', '东方树叶 2号配方', 1000, 10, 'kg', 1);
INSERT INTO [bom] (pid, code, name, quantity, tolerance, unit, enable) VALUES (3, 's-BSRC101', '水溶C100 1号配方', 1000, 10, 'kg', 1);
INSERT INTO [bom] (pid, code, name, quantity, tolerance, unit, enable) VALUES (3, 's-BSRC102', '水溶C100 2号配方', 1000, 10, 'kg', 1);

-- 农夫果园 配方项
INSERT INTO [bom_item] (pid, bid, mid, quantity, tolerance, unit) VALUES (1, 1, 1, 20, 2, 'kg');
INSERT INTO [bom_item] (pid, bid, mid, quantity, tolerance, unit) VALUES (1, 1, 2, 15, 1.5, 'kg');
INSERT INTO [bom_item] (pid, bid, mid, quantity, tolerance, unit) VALUES (1, 1, 3, 1,  0.1, 'kg');

INSERT INTO [bom_item] (pid, bid, mid, quantity, tolerance, unit) VALUES (1, 2, 1, 22, 2.2, 'kg');
INSERT INTO [bom_item] (pid, bid, mid, quantity, tolerance, unit) VALUES (1, 2, 2, 12, 1.2, 'kg');
INSERT INTO [bom_item] (pid, bid, mid, quantity, tolerance, unit) VALUES (1, 2, 3, 1.5,0.15, 'kg');

-- 东方树叶 配方项
INSERT INTO [bom_item] (pid, bid, mid, quantity, tolerance, unit) VALUES (2, 3, 4, 30, 3, 'kg');
INSERT INTO [bom_item] (pid, bid, mid, quantity, tolerance, unit) VALUES (2, 3, 2, 60, 6, 'kg');
INSERT INTO [bom_item] (pid, bid, mid, quantity, tolerance, unit) VALUES (2, 3, 3, 1.5,0.15, 'kg');

INSERT INTO [bom_item] (pid, bid, mid, quantity, tolerance, unit) VALUES (2, 4, 4, 32, 3.2, 'kg');
INSERT INTO [bom_item] (pid, bid, mid, quantity, tolerance, unit) VALUES (2, 4, 2, 80, 8, 'kg');
INSERT INTO [bom_item] (pid, bid, mid, quantity, tolerance, unit) VALUES (2, 4, 3, 1.5,0.15, 'kg');

-- 水溶C100 配方项
INSERT INTO [bom_item] (pid, bid, mid, quantity, tolerance, unit) VALUES (3, 5, 5, 52, 5.2, 'kg');
INSERT INTO [bom_item] (pid, bid, mid, quantity, tolerance, unit) VALUES (3, 5, 2, 16, 1.6, 'kg');
INSERT INTO [bom_item] (pid, bid, mid, quantity, tolerance, unit) VALUES (3, 5, 3, 1.5,0.15, 'kg');

INSERT INTO [bom_item] (pid, bid, mid, quantity, tolerance, unit) VALUES (3, 6, 5, 56, 5.6, 'kg');
INSERT INTO [bom_item] (pid, bid, mid, quantity, tolerance, unit) VALUES (3, 6, 2, 16, 1.6, 'kg');
INSERT INTO [bom_item] (pid, bid, mid, quantity, tolerance, unit) VALUES (3, 6, 3, 1,  0.1, 'kg');

-- ----------------------------------------------------------------------------
--  工艺

INSERT INTO [process_flow] (pid, lid, bid, code, name, type, enable) VALUES (1, 1, 1, 'F1NFGY0101', '农夫果园 配方1 工艺1',  1, 1); -- 无序投料
INSERT INTO [process_flow] (pid, lid, bid, code, name, type, enable) VALUES (1, 1, 1, 'F1NFGY0102', '农夫果园 配方1 工艺2',  2, 1); -- 有序投料
INSERT INTO [process_flow] (pid, lid, bid, code, name, type, enable) VALUES (1, 1, 2, 'F1NFGY0201', '农夫果园 配方2 工艺1',  1, 1); -- 无序投料
INSERT INTO [process_flow] (pid, lid, bid, code, name, type, enable) VALUES (1, 1, 2, 'F1NFGY0202', '农夫果园 配方2 工艺2',  2, 1); -- 有序投料

INSERT INTO [process_flow] (pid, lid, bid, code, name, type, enable) VALUES (2, 2, 3, 'F2DFSY0101', '东方树叶 配方1 工艺1',  1, 1); -- 无序投料
INSERT INTO [process_flow] (pid, lid, bid, code, name, type, enable) VALUES (2, 2, 3, 'F2DFSY0102', '东方树叶 配方1 工艺2',  2, 1); -- 有序投料
INSERT INTO [process_flow] (pid, lid, bid, code, name, type, enable) VALUES (2, 2, 4, 'F2DFSY0201', '东方树叶 配方2 工艺1',  1, 1); -- 无序投料
INSERT INTO [process_flow] (pid, lid, bid, code, name, type, enable) VALUES (2, 2, 4, 'F2DFSY0202', '东方树叶 配方2 工艺2',  2, 1); -- 有序投料

INSERT INTO [process_flow] (pid, lid, bid, code, name, type, enable) VALUES (3, 1, 5, 'F1SRC10101', '水溶C100 配方1 工艺1',  1, 1); -- 无序投料
INSERT INTO [process_flow] (pid, lid, bid, code, name, type, enable) VALUES (3, 1, 5, 'F1SRC10102', '水溶C100 配方1 工艺2',  2, 1); -- 有序投料
INSERT INTO [process_flow] (pid, lid, bid, code, name, type, enable) VALUES (3, 1, 6, 'F1SRC10201', '水溶C100 配方2 工艺1',  1, 1); -- 无序投料
INSERT INTO [process_flow] (pid, lid, bid, code, name, type, enable) VALUES (3, 1, 6, 'F1SRC10202', '水溶C100 配方2 工艺2',  2, 1); -- 有序投料

-- 农夫果园 工艺项 -- 无序投料
INSERT INTO [process_flow_item] (fid, pid, lid, bid, mid, eqpt_type, gate_type, sequence, interval, enable) VALUES (1, 1, 1, 1, 1, '搅拌罐', '大投料口', 0, 0, 1); 
INSERT INTO [process_flow_item] (fid, pid, lid, bid, mid, eqpt_type, gate_type, sequence, interval, enable) VALUES (1, 1, 1, 1, 2, '搅拌罐', '大投料口', 0, 0, 1);
INSERT INTO [process_flow_item] (fid, pid, lid, bid, mid, eqpt_type, gate_type, sequence, interval, enable) VALUES (1, 1, 1, 1, 3, '搅拌罐', '大投料口', 0, 0, 1);

-- 农夫果园 工艺项 -- 有序投料
INSERT INTO [process_flow_item] (fid, pid, lid, bid, mid, eqpt_type, gate_type, sequence, interval, enable) VALUES (2, 1, 1, 2, 1, '加热罐', '大投料口', 1, 10, 1); 
INSERT INTO [process_flow_item] (fid, pid, lid, bid, mid, eqpt_type, gate_type, sequence, interval, enable) VALUES (2, 1, 1, 2, 2, '加热罐', '大投料口', 2, 30, 1);
INSERT INTO [process_flow_item] (fid, pid, lid, bid, mid, eqpt_type, gate_type, sequence, interval, enable) VALUES (2, 1, 1, 2, 3, '加热罐', '大投料口', 3, 20, 1);

-- 东方树叶 工艺项 -- 无序投料
INSERT INTO [process_flow_item] (fid, pid, lid, bid, mid, eqpt_type, gate_type, sequence, interval, enable) VALUES (3, 2, 2, 3, 1, '搅拌罐', '大投料口', 0, 0, 1); 
INSERT INTO [process_flow_item] (fid, pid, lid, bid, mid, eqpt_type, gate_type, sequence, interval, enable) VALUES (3, 2, 2, 3, 2, '搅拌罐', '大投料口', 0, 0, 1);
INSERT INTO [process_flow_item] (fid, pid, lid, bid, mid, eqpt_type, gate_type, sequence, interval, enable) VALUES (3, 2, 2, 3, 3, '搅拌罐', '大投料口', 0, 0, 1);

-- 东方树叶 工艺项 -- 有序投料
INSERT INTO [process_flow_item] (fid, pid, lid, bid, mid, eqpt_type, gate_type, sequence, interval, enable) VALUES (4, 2, 2, 4, 1, '搅拌罐', '大投料口', 1, 30, 1); 
INSERT INTO [process_flow_item] (fid, pid, lid, bid, mid, eqpt_type, gate_type, sequence, interval, enable) VALUES (4, 2, 2, 4, 2, '搅拌罐', '大投料口', 2, 30, 1);
INSERT INTO [process_flow_item] (fid, pid, lid, bid, mid, eqpt_type, gate_type, sequence, interval, enable) VALUES (4, 2, 2, 4, 3, '搅拌罐', '大投料口', 3, 60, 1);

-- 水溶C100 工艺项 -- 无序投料
INSERT INTO [process_flow_item] (fid, pid, lid, bid, mid, eqpt_type, gate_type, sequence, interval, enable) VALUES (5, 3, 1, 5, 1, '搅拌罐', '大投料口', 0, 0, 1); 
INSERT INTO [process_flow_item] (fid, pid, lid, bid, mid, eqpt_type, gate_type, sequence, interval, enable) VALUES (5, 3, 1, 5, 2, '搅拌罐', '大投料口', 0, 0, 1);
INSERT INTO [process_flow_item] (fid, pid, lid, bid, mid, eqpt_type, gate_type, sequence, interval, enable) VALUES (5, 3, 1, 5, 3, '搅拌罐', '大投料口', 0, 0, 1);

-- 水溶C100 工艺项 -- 有序投料
INSERT INTO [process_flow_item] (fid, pid, lid, bid, mid, eqpt_type, gate_type, sequence, interval, enable) VALUES (6, 3, 1, 6, 1, '搅拌罐', '大投料口', 1, 30, 1); 
INSERT INTO [process_flow_item] (fid, pid, lid, bid, mid, eqpt_type, gate_type, sequence, interval, enable) VALUES (6, 3, 1, 6, 2, '搅拌罐', '大投料口', 2, 30, 1);
INSERT INTO [process_flow_item] (fid, pid, lid, bid, mid, eqpt_type, gate_type, sequence, interval, enable) VALUES (6, 3, 1, 6, 3, '搅拌罐', '大投料口', 3, 60, 1);


-- 计划
INSERT INTO [schedule] (lid, pid, bid, fid, code, quantity, unit, schd_sdate, schd_edate, schd_time, appr_time, schd_uid, appr_uid) VALUES (1, 1, 1, 1, 'S140606001', 3200, 'kl',
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


-- 工单
INSERT INTO [workorder] (sid, lid, eid, pid, bid, fid, code, sequence, quantity, unit, work_sdate, work_edate, status, owner_uid, weighing_uid, operator_uid) VALUES (1, 1, 1, 1, 1, 1, 'W140606001', 1, 32000, 'kl', 
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-06 08:00:00'),
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-06 17:30:00'), 9, 1, 1, 2); -- 完成
INSERT INTO [workorder] (sid, lid, eid, pid, bid, fid, code, sequence, quantity, unit, work_sdate, work_edate, status, owner_uid, weighing_uid, operator_uid) VALUES (1, 1, 2, 1, 1, 1, 'W140607001', 2, 32000, 'kl',
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-07 08:00:00'),
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-07 17:30:00'), 5, 1, 1, 2); -- 已投
INSERT INTO [workorder] (sid, lid, eid, pid, bid, fid, code, sequence, quantity, unit, work_sdate, work_edate, status, owner_uid, weighing_uid, operator_uid) VALUES (1, 1, 3, 1, 1, 1, 'W140608001', 3, 32000, 'kl',
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-08 08:00:00'),
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-08 17:30:00'), 4, 1, 1, 2); -- 已领
INSERT INTO [workorder] (sid, lid, eid, pid, bid, fid, code, sequence, quantity, unit, work_sdate, work_edate, status, owner_uid, weighing_uid, operator_uid) VALUES (1, 1, 1, 1, 1, 1, 'W140609001', 4, 32000, 'kl',
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-09 08:00:00'),
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-09 17:30:00'), 3, 1, 1, 2); -- 已复
INSERT INTO [workorder] (sid, lid, eid, pid, bid, fid, code, sequence, quantity, unit, work_sdate, work_edate, status, owner_uid, weighing_uid, operator_uid) VALUES (1, 1, 2, 1, 2, 4, 'W140610001', 5, 32000, 'kl',
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-10 08:00:00'),
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-10 17:30:00'), 2, 1, 1, 2); -- 已配
INSERT INTO [workorder] (sid, lid, eid, pid, bid, fid, code, sequence, quantity, unit, work_sdate, work_edate, status, owner_uid, weighing_uid, operator_uid) VALUES (1, 1, 3, 1, 1, 1, 'W140611001', 6, 32000, 'kl',
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-11 08:00:00'),
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-11 17:30:00'), 1, 1, 1, 2); -- 已审
INSERT INTO [workorder] (sid, lid, eid, pid, bid, fid, code, sequence, quantity, unit, work_sdate, work_edate, status, owner_uid, weighing_uid, operator_uid) VALUES (1, 1, 1, 1, 1, 1, 'W140612001', 7, 32000, 'kl',
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-12 08:00:00'),
DATEDIFF(s, '1970-01-01 00:00:00', '2014-06-12 17:30:00'), 0, 1, 1, 2); -- 未执行
--
SELECT sid, fid, pid, lid, bid, code, sequence, quantity, unit, work_sdate, work_edate, status, owner_uid, weighing_uid, operator_uid,
	DATEADD(s, work_sdate, '1970-01-01 00:00:00') as work_sdate_s,
	DATEADD(s, work_edate, '1970-01-01 00:00:00') as work_edate_s
FROM [workorder];
--


-- 工单原料
-- 工单1完成
INSERT INTO [workorder_material] (sid, wid, mid, lid, eid, pid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (1, 1, 1, 1, 1, 1, 32*20+3,  32*20+0.5, 32*20, 1, 3, 'kg', 5);
INSERT INTO [workorder_material] (sid, wid, mid, lid, eid, pid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (1, 1, 2, 1, 1, 1, 32*15+3,  32*15+0.5, 32*15, 1, 3, 'kg', 5);
INSERT INTO [workorder_material] (sid, wid, mid, lid, eid, pid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (1, 1, 3, 1, 1, 1, 32*1+1,   32*1+0.2,  32*1,  1, 3, 'kg', 5);

-- 工单2已投 (客户端没有设置完成标记，原因为：投料后还需要其他工艺处理时间)
INSERT INTO [workorder_material] (sid, wid, mid, lid, eid, pid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (1, 2, 1, 1, 2, 1, 32*20+3,  32*20+0.5, 32*20, 1, 3, 'kg', 5);
INSERT INTO [workorder_material] (sid, wid, mid, lid, eid, pid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (1, 2, 2, 1, 2, 1, 32*15+3,  32*15+0.5, 32*15, 1, 3, 'kg', 5);
INSERT INTO [workorder_material] (sid, wid, mid, lid, eid, pid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (1, 2, 3, 1, 2, 1, 32*1+1,   32*1+0.2,  32*1,  1, 3, 'kg', 5);

-- 工单3已领 (所有工单原料status>=4, 并且存在至少一种原料status=4)
INSERT INTO [workorder_material] (sid, wid, mid, lid, eid, pid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (1, 3, 1, 1, 3, 1, 32*20+3,  32*20+0.5, 32*20, 1, 3, 'kg', 4);
INSERT INTO [workorder_material] (sid, wid, mid, lid, eid, pid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (1, 3, 2, 1, 3, 1, 32*15+3,  32*15+0.5, 32*15, 1, 3, 'kg', 4);
INSERT INTO [workorder_material] (sid, wid, mid, lid, eid, pid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (1, 3, 3, 1, 3, 1, 32*1+1,   32*1+0.2,  32*1,  1, 3, 'kg', 5); -- 该原料全部投完，工单其他原料没有全部投完

-- 工单4已复核
INSERT INTO [workorder_material] (sid, wid, mid, lid, eid, pid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (1, 4, 1, 1, 1, 1, 32*20*1.0009+4,  32*22*1.0009,	  32*22,  1, 3, 'kg', 3);
INSERT INTO [workorder_material] (sid, wid, mid, lid, eid, pid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (1, 4, 2, 1, 1, 1, 32*15*1.0009+4,  32*12*1.0009,	  32*12,  1, 3, 'kg', 3);
INSERT INTO [workorder_material] (sid, wid, mid, lid, eid, pid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (1, 4, 3, 1, 1, 1, 32*1.5*1.0009+1,   32*1.5*1.0009,  32*1.5, 1, 3, 'kg', 3);

-- 工单5已配
INSERT INTO [workorder_material] (sid, wid, mid, lid, eid, pid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (1, 5, 1, 1, 2, 1, 32*20+4,  32*20+0.5, 32*20, 1, 3, 'kg', 2);
INSERT INTO [workorder_material] (sid, wid, mid, lid, eid, pid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (1, 5, 2, 1, 2, 1, 32*15+4,  32*15+0.5, 32*15, 1, 3, 'kg', 2);
INSERT INTO [workorder_material] (sid, wid, mid, lid, eid, pid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (1, 5, 3, 1, 2, 1, 32*1+1,   32*1+0.2,  32*1,  1, 3, 'kg', 2);

-- 工单6已审
INSERT INTO [workorder_material] (sid, wid, mid, lid, eid, pid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (1, 6, 1, 1, 3, 1, 0,  0, 32*20, 32*20*0.001, 0, 'kg', 1);
INSERT INTO [workorder_material] (sid, wid, mid, lid, eid, pid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (1, 6, 2, 1, 3, 1, 0,  0, 32*15, 32*15*0.001, 0, 'kg', 1);
INSERT INTO [workorder_material] (sid, wid, mid, lid, eid, pid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (1, 6, 3, 1, 3, 1, 0,  0, 32*1,  32*1*0.001, 0, 'kg', 1);

-- 工单7未执行
INSERT INTO [workorder_material] (sid, wid, mid, lid, eid, pid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (1, 7, 1, 1, 1, 1, 32*20+4,  32*20+0.5, 32*20, 1, 0, 'kg', 0);
INSERT INTO [workorder_material] (sid, wid, mid, lid, eid, pid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (1, 7, 2, 1, 1, 1, 32*15+4,  32*15+0.5, 32*15, 1, 0, 'kg', 0);
INSERT INTO [workorder_material] (sid, wid, mid, lid, eid, pid, actl_total, actl_quantity, quantity, tolerance, container_qty, unit, status) VALUES (1, 7, 3, 1, 1, 1, 32*1+1,   32*1+0.2,  32*1,  1, 0, 'kg', 0);

-- 工单原料桶
-- 工单1完成 s-番茄浓缩液 32*20 = 640 kg, 每个投料口投2桶，每桶80kg左右。
INSERT INTO [workorder_container] (sid, wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (1, 1, 1, 1, 1,		1, 1, 1, 1,
1, 80.1+3, 80.1, 'kg', 5);
INSERT INTO [workorder_container] (sid, wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (1, 1, 1, 1, 1,		1, 1, 1, 1,
2, 80+3, 80.0, 'kg', 5);
INSERT INTO [workorder_container] (sid, wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (1, 1, 1, 1, 1,		1, 1, 2, 2,
3, 80.1+3, 80.1, 'kg', 5);
INSERT INTO [workorder_container] (sid, wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (1, 1, 1, 1, 1,		1, 1, 2, 2,
4, 80.3+3, 80.3, 'kg', 5);
INSERT INTO [workorder_container] (sid, wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (1, 1, 1, 1, 1,		1, 1, 3, 3,
5, 80.1+3, 80.1, 'kg', 5);
INSERT INTO [workorder_container] (sid, wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (1, 1, 1, 1, 1,		1, 1, 3, 3,
6, 80+3, 80.0, 'kg', 5);
INSERT INTO [workorder_container] (sid, wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (1, 1, 1, 1, 1,		1, 1, 4, 4,
7, 80.1+3, 80.1, 'kg', 5);
INSERT INTO [workorder_container] (sid, wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (1, 1, 1, 1, 1,		1, 1, 4, 4,
8, 80.3+3, 80.3, 'kg', 5);

-- 工单1完成 s-香精A号 32*15 = 480 kg, 每个投料口投1桶，每桶120kg左右。
INSERT INTO [workorder_container] (sid, wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (1, 1, 2, 1, 1,		1, 1, 1, 1,
1, 120.0+5, 120.0, 'kg', 5);
INSERT INTO [workorder_container] (sid, wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (1, 1, 2, 1, 1,		1, 1, 2, 2,
2, 120.3+5, 120.3, 'kg', 5);
INSERT INTO [workorder_container] (sid, wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (1, 1, 2, 1, 1,		1, 1, 3, 3,
3, 120.8+5, 119.8, 'kg', 5);
INSERT INTO [workorder_container] (sid, wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (1, 1, 2, 1, 1,		1, 1, 4, 4,
4, 120.1+5, 120.1, 'kg', 5);

-- 工单1完成 s-香料 32*1 = 32 kg, 2个投料口投1包，每包16kg左右。
INSERT INTO [workorder_container] (sid, wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (1, 1, 3, 1, 1,		1, 1, 1, 1,
1, 16.3+0.3, 16.3, 'kg', 5);
INSERT INTO [workorder_container] (sid, wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (1, 1, 3, 1, 1,		1, 1, 3, 3,
2, 16.1+0.3, 16.1, 'kg', 5);


-- 工单原料桶
-- 工单4已复核 s-番茄浓缩液 32*22 = 704 kg, 每个投料口投1桶，每桶176kg左右。
INSERT INTO [workorder_container] (sid, wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (1, 4, 1, 1, 2,		1, 1, 1, 1,
1, 176+3, 176, 'kg', 3);
INSERT INTO [workorder_container] (sid, wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (1, 4, 1, 1, 2,		1, 1, 2, 2,
2, 176+3, 176, 'kg', 3);
INSERT INTO [workorder_container] (sid, wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (1, 4, 1, 1, 2,		1, 1, 3, 3,
3, 176+3, 176, 'kg', 3);
INSERT INTO [workorder_container] (sid, wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (1, 4, 1, 1, 2,		1, 1, 4, 4,
4, 176+3, 176, 'kg', 3);

-- 工单4已复核 s-香精A号 32*12 = 384 kg, 每个投料口投1包，每包96kg左右。
INSERT INTO [workorder_container] (sid, wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (1, 4, 2, 1, 2,		1, 1, 1, 1,
1, 96.0+5, 96.0, 'kg', 3);
INSERT INTO [workorder_container] (sid, wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (1, 4, 2, 1, 2,		1, 1, 2, 2,
2, 96.3+5, 96.3, 'kg', 3);
INSERT INTO [workorder_container] (sid, wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (1, 4, 2, 1, 2,		1, 1, 3, 3,
3, 96.8+5, 96.8, 'kg', 3);
INSERT INTO [workorder_container] (sid, wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (1, 4, 2, 1, 2,		1, 1, 4, 4,
4, 96.1+5, 96.1, 'kg', 3);

-- 工单4已复核 s-香料 32*1.5 = 48 kg, 每个投料口投1包，每包12kg左右。
INSERT INTO [workorder_container] (sid, wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (1, 4, 3, 1, 2,		1, 1, 1, 1,
1, 12.3+0.3, 12.3, 'kg', 3);
INSERT INTO [workorder_container] (sid, wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (1, 4, 3, 1, 2,		1, 1, 2, 2,
2, 12.1+0.3, 12.1, 'kg', 3);
INSERT INTO [workorder_container] (sid, wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (1, 4, 3, 1, 2,		1, 1, 3, 3,
3, 12.3+0.3, 12.3, 'kg', 3);
INSERT INTO [workorder_container] (sid, wid, mid, fid, bid, lid, pid, eid, gid, [sequence], total, quantity, unit, status) VALUES (1, 4, 3, 1, 2,		1, 1, 4, 4,
4, 12.1+0.3, 12.1, 'kg', 3);

-- ----------------------------------------------------------------------------
-- End of Initiailize Data
-- ----------------------------------------------------------------------------
