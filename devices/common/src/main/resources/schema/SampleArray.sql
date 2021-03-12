CREATE TABLE SampleArray (
	unique_device_identifier varchar(64),
	metric_id varchar(64),
	vendor_metric_id varchar(64),
	instance_id bigint,
	unit_id varchar(64),
	frequency bigint,
	values blob,
	device_time_sec bigint,
	device_time_nanosec bigint,
	presentation_time_sec bigint,
	presentation_time_nanosec bigint
);