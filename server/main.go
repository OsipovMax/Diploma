package main

import (
	"database/sql"
	"encoding/json"
	"fmt"
	"log"
	"net/http"

	"github.com/gorilla/mux"
	_ "github.com/mattn/go-sqlite3"
)

// InformationDevice is a table title
const InformationDevice = "DeviceInformation"

func getConnectionDB() *sql.DB {
	db, err := sql.Open("sqlite3", "db.db")
	if err != nil {
		panic(err)
	}
	return db
}

func returnConnectionDB(db *sql.DB) {
	err := db.Close()
	if err != nil {
		fmt.Println("close connection problem")
		panic(err)
	}
}

func getBorderlineResults(db *sql.DB, title string) (DeviceRecord, DeviceRecord) {
	var bestTestResult DeviceRecord
	var worstTestResult DeviceRecord
	// worst result
	row, err := db.Query("SELECT DeviceTitle, TestResults FROM " + title + " ORDER BY TestResults ASC LIMIT 1")
	if err != nil {
		panic(err)
	}
	for row.Next() {
		err := row.Scan(&worstTestResult.DeviceTitle, &worstTestResult.Result)
		if err != nil {
			fmt.Println(err)
			continue
		}
	}
	row.Close()
	//best result
	row, err = db.Query("SELECT DeviceTitle, TestResults FROM " + title + " ORDER BY TestResults DESC LIMIT 1")
	if err != nil {
		panic(err)
	}
	defer row.Close()
	for row.Next() {
		err := row.Scan(&bestTestResult.DeviceTitle, &bestTestResult.Result)
		if err != nil {
			fmt.Println(err)
			continue
		}
	}
	return worstTestResult, bestTestResult
}

func checkRecord(title string, deviceRecord DeviceRecord) bool {
	db := getConnectionDB()
	defer returnConnectionDB(db)
	rows, err := db.Query("SELECT ID, TestResults FROM "+title+" WHERE DeviceTitle LIKE $1", "%"+deviceRecord.DeviceTitle+"%")
	if err != nil {
		panic(err)
	}
	defer rows.Close()
	for rows.Next() {
		var idVal = 0
		var resultVal = 0.0
		rows.Scan(&idVal, &resultVal)
		if resultVal < deviceRecord.Result {
			rows.Close()
			_, err := db.Exec("UPDATE "+title+" SET TestResults = $1 WHERE ID = $2", deviceRecord.Result, idVal)
			if err != nil {
				fmt.Println("EROOOOOOOOOR")
				panic(err)
			}
		}
		return true
	}
	return false
}

func insertRecord(title string, deviceRecord DeviceRecord) {
	db := getConnectionDB()

	_, err := db.Exec("insert into "+title+"(DeviceTitle, TestResults) values ($1, $2)",
		deviceRecord.DeviceTitle, deviceRecord.Result)

	if err != nil {
		panic(err)
	}

	rows, err := db.Query("SELECT Model FROM "+InformationDevice+" WHERE Model LIKE $1", "%"+deviceRecord.DeviceInformation.DeviceModel+"%")
	if err != nil {
		panic(err)
	}
	defer rows.Close()
	for rows.Next() {
		return
	}
	_, err = db.Exec("insert into DeviceInformation (Brand, Model, OS, CPU, RAM, MEMORY) values ($1, $2, $3, $4, $5, $6)",
		deviceRecord.DeviceInformation.DeviceBrand, deviceRecord.DeviceInformation.DeviceModel, deviceRecord.DeviceInformation.OS,
		deviceRecord.DeviceInformation.CPU, deviceRecord.DeviceInformation.RAM, deviceRecord.DeviceInformation.MEMORY)

	if err != nil {
		panic(err)
	}
}

func getDataFromDb(title string) TestResults {
	var testResults TestResults
	db := getConnectionDB()

	rows, err := db.Query("SELECT * FROM " + title + " JOIN " + InformationDevice +
		" ON " + title + ".DeviceTitle = " + "(" +
		InformationDevice + ".Brand " + "|| ' ' || " + InformationDevice + ".Model" + ") " +
		"ORDER BY TestResults DESC")

	if err != nil {
		panic(err)
	}

	defer rows.Close()

	for rows.Next() {
		devInformation := DeviceInformation{}
		deviceRecord := DeviceRecord{}
		var tmp int
		err := rows.Scan(&deviceRecord.ID, &deviceRecord.DeviceTitle, &deviceRecord.Result, &tmp, &devInformation.DeviceBrand, &devInformation.DeviceModel,
			&devInformation.OS, &devInformation.CPU, &devInformation.RAM, &devInformation.MEMORY)
		if err != nil {
			fmt.Println(err)
			continue
		}
		deviceRecord.DeviceInformation = &devInformation
		testResults.Devices = append(testResults.Devices, deviceRecord)
	}
	testResults.WorstDevice, testResults.BestDevice = getBorderlineResults(db, title)
	return testResults
}

func getTestsResults(w http.ResponseWriter, r *http.Request) {
	var testResults TestResults
	vars := mux.Vars(r)
	//get table name from request
	tablTitle := vars["test"] + vars["mode"] + "Table"
	testResults = getDataFromDb(tablTitle)
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(testResults)
}

func createResultRecord(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	var deviceRecord DeviceRecord
	vars := mux.Vars(r)
	tableTitle := vars["test"] + vars["mode"] + "Table"
	err := json.NewDecoder(r.Body).Decode(&deviceRecord)
	if err != nil {
		panic(err)
	}
	if checkRecord(tableTitle, deviceRecord) != true {
		insertRecord(tableTitle, deviceRecord)
	}
	json.NewEncoder(w).Encode(deviceRecord)
}

func getBorderlineValues(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	db := getConnectionDB()
	var borderlineResult BorderlineResults
	vars := mux.Vars(r)
	borderlineResult.WorstDevice, borderlineResult.BestDevice = getBorderlineResults(db, vars["test"]+"ProcTable")
	borderlineResult.WorstDeviceMem, borderlineResult.BestDeviceMem = getBorderlineResults(db, vars["test"]+"MemTable")
	json.NewEncoder(w).Encode(borderlineResult)
}

func main() {
	router := mux.NewRouter()
	router.HandleFunc("/leaderBoard/{test}/{mode}", getTestsResults).Methods("GET")
	router.HandleFunc("/{test}/{mode}", createResultRecord).Methods("POST")
	router.HandleFunc("/{test}", getBorderlineValues).Methods("GET")
	//router.HandleFunc("/wallTest", getBorderlineDevicesWallTest).Methods("GET") // ???
	//log.Fatal(http.ListenAndServe("192.168.0.43:8000", router))
	log.Fatal(http.ListenAndServe("10.0.3.79:12333", router))
}
