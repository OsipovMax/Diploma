package main

import (
	"bytes"
	"encoding/json"
	"fmt"
	"net/http"
	"os"
	"os/exec"
	"strconv"

	"github.com/shirou/gopsutil/cpu"
	"github.com/shirou/gopsutil/host"
	"github.com/shirou/gopsutil/mem"
)

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

func inputDataValidation(testName string, testMode string) bool {
	tests := [3]string{"Wall", "Bandwidth", "Search"}
	modes := [2]string{"Proc", "Mem"}
	for i := 0; i < len(tests); i++ {
		if tests[i] == testName {
			break
		} else {
			return false
		}
	}
	for i := 0; i < len(modes); i++ {
		if modes[i] == testMode {
			break
		} else {
			return false
		}
	}
	return true
}

func executeTest(testName string, testMode string) string {
	out, _ := exec.Command(testName+".exe", testMode).Output()
	return string(out)
}

func getDeviceInformation(manufacter string, model string) DeviceInformation {
	var devInform DeviceInformation
	hostInfo, _ := host.Info()
	memInfo, _ := mem.VirtualMemory()
	cpuInfo, _ := cpu.Info()
	devInform.CPU = cpuInfo[0].ModelName
	devInform.RAM = strconv.FormatUint(memInfo.Total, 10)
	devInform.OS = hostInfo.OS
	devInform.DeviceBrand = manufacter
	devInform.DeviceModel = model
	return devInform
}

func main() {
	var manufacter string
	var model string
	testName := os.Args[1]
	testMode := os.Args[2]
	if !inputDataValidation(testName, testMode) {
		fmt.Println("No valid input data")
		return
	}
	fmt.Println("Введите название производителя вашего вычислительного устройства: ")
	fmt.Fscan(os.Stdin, &manufacter)
	fmt.Println("Введите название модель вашего вычислительного устройства: ")
	fmt.Fscan(os.Stdin, &model)

	var devRecord DeviceRecord
	client := http.Client{}
	devInform := getDeviceInformation(manufacter, model)
	devRecord.DeviceInformation = &devInform
	devRecord.DeviceTitle = manufacter + " " + model
	devRecord.Result, _ = strconv.ParseFloat(executeTest(testName, testMode), 64)
	var record []byte
	record, _ = json.Marshal(devRecord)
	client.Post("http://192.168.0.43:8000/"+testName+"/"+testMode, "application/json", bytes.NewBuffer(record))
}
