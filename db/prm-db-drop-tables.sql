-- ----------------------------------------------------------------------------
-- PRM MS SERVER SQL SCRIPT
-- V1.0 2014-6-4
-- V1.1 2014-6-11
-- V1.2 2014-6-12
-- V1.2.1 2014-6-15, add workorder_material.tolerance
-- V1.2.2 2014-6-15, add intialized data of weighing_room, workorder_container
-- V1.2.3 2014-6-15, remove NOT NULL for workorder_log.uid
-- V1.2.4 2014-6-20, split to drop tables file & init talbes file
-- ----------------------------------------------------------------------------

-- ----------------------------------------------------------------------------
-- Drop Tables 
-- ----------------------------------------------------------------------------
DROP TABLE store_requisition_item
DROP TABLE store_requisition
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
