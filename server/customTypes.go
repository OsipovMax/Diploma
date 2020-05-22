package main

// DeviceInformation is ...
type DeviceInformation struct {
	DeviceBrand string `json:"Brand"`
	DeviceModel string `json:"Model"`
	OS          string `json:"os"`
	CPU         string `json:"cpu"`
	RAM         string `json:"ram"`
	MEMORY      string `json:"memory"`
}

// DeviceRecord is ...
type DeviceRecord struct {
	ID                int                `json:"id"`
	DeviceTitle       string             `json:"device"`
	Result            float64            `json:"result"`
	DeviceInformation *DeviceInformation `json:"deviceInformation"`
}

// TestResults is ...
type TestResults struct {
	BestDevice  DeviceRecord   `json:"bestDevice"`
	WorstDevice DeviceRecord   `json:"worstDevice"`
	Devices     []DeviceRecord `json:"devicesList"`
}

// BorderlineResults is ...
type BorderlineResults struct {
	BestDevice     DeviceRecord `json:"bestDevice"`
	WorstDevice    DeviceRecord `json:"worstDevice"`
	BestDeviceMem  DeviceRecord `json:"bestDeviceMem"`
	WorstDeviceMem DeviceRecord `json:"worstDeviceMem"`
}
