use field_agent_test;
drop procedure if exists set_known_good_state;
-- change default delimiter
delimiter //
create procedure set_known_good_state()
begin
    delete from location;
    alter table location auto_increment = 1;
    delete from agency_agent;
    delete from agency;
    alter table agency auto_increment = 1;
    delete from alias;
    alter table alias auto_increment = 1;
    delete from agent;
    alter table agent auto_increment = 1;
    
    -- new code: continue to delete table data as required
    -- keeping key dependencies in mind
    delete from security_clearance;
    alter table security_clearance auto_increment = 1;
    
    
    -- new code: security_clearance is required below...
    insert into security_clearance values
        (1, 'Secret'),
        (2, 'Top Secret');
    
    insert into agency(agency_id, short_name, long_name) values
        (1, 'ACME', 'Agency to Classify & Monitor Evildoers'),
        (2, 'MASK', 'Mobile Armored Strike Kommand'),
        (3, 'ODIN', 'Organization of Democratic Intelligence Networks');
        
    insert into location (location_id, name, address, city, region, country_code, postal_code, agency_id)
        values
    (1, 'HQ', '123 Elm', 'Des Moines', 'IA', 'USA', '55555', 1),
    (2, 'Safe House #1', 'A One Ave.', 'Walla Walla', 'WA', 'USA', '54321-1234', 1),
    (3, 'HQ', '123 Elm', 'Test', 'WI', 'USA', '55555', 2),
    (4, 'Remote', '999 Nine St.', 'Test', 'WI', 'USA', '55555', 2),
    (5, 'HQ', '123 Elm', 'Test', 'WI', 'USA', '55555', 3), -- for delete tests
    (6, 'Remote', '999 Nine St.', 'Test', 'WI', 'USA', '55555', 3);
        
    insert into agent 
        (first_name, middle_name, last_name, dob, height_in_inches) 
    values
        ('Hazel','C','Sauven','1954-09-16',76),
        ('Claudian','C','O''Lynn','1956-11-09',41),
        ('Winn','V','Puckrin','1999-10-21',60),
        ('Kiab','U','Whitham','1960-07-29',52),
        ('Min','E','Dearle','1967-04-18',44),
        ('Urban','H','Carwithen',null,58),
        ('Ulises','B','Muhammad','2008-04-01',80),
        ('Phylys','Y','Howitt','1979-03-28',68);
        
	insert into alias values
		(1, 'Harold', '', 1),
		(2, 'James', 'Prefers martinis shaken, not stirred', 1),
        (3, 'Bob', 'Not a cable guy', 1);
        
    insert into agency_agent 
        (agency_id, agent_id, identifier, security_clearance_id, activation_date)
    select
        agency.agency_id,                              -- agency_id
        agent.agent_id,                                -- agent_id
        concat(agency.agency_id, '-', agent.agent_id), -- identifier
        1,                                             -- security_clearance_id
        date_add(agent.dob, interval 10 year)          -- activation_date
    from agency
    inner join agent
    where agent.agent_id not in (6, 8)
    and agency.agency_id != 2;
end //
-- Change the statement terminator back to the original.
delimiter ;
