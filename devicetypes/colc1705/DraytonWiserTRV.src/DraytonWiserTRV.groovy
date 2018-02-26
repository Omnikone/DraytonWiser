/**
 *  Temp
 *
 *  Copyright 2018 Colin Chapman
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
metadata {
	definition (name: "Drayton Wiser TRV", namespace: "colc1705", author: "Colin Chapman") {
		capability "Thermostat"
        capability "Sensor"
        capability "Refresh"
        
        attribute "mode", "string"
        
        command "spDown"
        command "spUp"
        command "manualMode"
        command "autoMode"
        command "test"
        
	}


	simulator {
		// TODO: define status and reply messages here
	}

	tiles {
		// TODO: define your main and details tiles here
        multiAttributeTile(name:"thermostatMulti", type:"thermostat", width:6, height:4) {
        	tileAttribute("device.temperature", key: "PRIMARY_CONTROL") {
            	attributeState("default", label:'${currentValue}°C', unit:"°C", backgroundColors:[
                    [value:  0, color: "#153591"],
                	[value:  7, color: "#1E9CBB"],
                	[value: 15, color: "#90D2A7"],
                	[value: 23, color: "#44B621"],
                	[value: 29, color: "#F1D801"],
                	[value: 33, color: "#D04E00"],
                	[value: 36, color: "#BC2323"]
                    ])
            }
            tileAttribute("device.heatingSetPoint", key: "VALUE_CONTROL") {
            	attributeState("VALUE_UP", action: "spUp")
                attributeState("VALUE_DOWN", action: "spDown")
            }
            
            tileAttribute("device.heatingSetPoint", key: "HEATING_SETPOINT") {
            	attributeState("default", label: '${currentValue}', unit: "°C")
            }
            
        
        }
        
        standardTile("mode", "device.mode", decoration: "flat", width: 2, height: 2) {
        	state "manual", label: '${currentValue}', action:"autoMode"
            state "auto", label: '${currentValue}', action:"manualMode"
        }
        
        standardTile("boost", "device.boost", decoration: "flat", width: 2, height: 2) {
        	state "off", label: '${currentValue}', action: "boost"
        }
        
        standardTile("test", "device.getHubConfig", decoration: "flat", height: 2, width: 2, inactiveLabel: false) {
            state "default", label:"Test", action:"test", icon:"", backgroundColor:"#FFFFFF"
        }
	}
}

def updated() {
	log.debug "updated()"
   
}

def parse(String description) {
	log.debug "parse()"
    
    
    

}

def test() {
	log.debug "test()"
    parent.test(device.deviceNetworkId)
}

def spUp() {
	log.debug "spUp()"
    def currentSP = device.currentState("heatingSetPoint").getDoubleValue()
    def newSP = currentSP + 0.5
    log.debug "Current setting: " + currentSP
    sendEvent(name: "heatingSetPoint", value: newSP, unit: "°C")
    parent.setPoint(device.deviceNetworkId, newSP)
    
}

def spDown() {
	log.debug "spDown()"
    def currentSP = device.currentState("heatingSetPoint").getDoubleValue()
    def newSP = currentSP - 0.5
    log.debug "Current setting: " + currentSP
    sendEvent(name: "heatingSetPoint", value: newSP, unit: "°C")
    parent.setPoint(device.deviceNetworkId, newSP)
}

def setTemp(temp, setPoint) {
 	log.debug device.name + " is " + temp + "°C"
    sendEvent(name: "temperature", value: temp, unit: "°C")
    sendEvent(name: "heatingSetPoint", value: setPoint, unit: "°C")
    
}

def setMode(mode) {
	log.debug "setMode($mode)"
    sendEvent(name: "mode", value: mode)
}
    
def autoMode() {
	log.debug "autoMode()"
    parent.setRoomManualMode(device.deviceNetworkId, false)
}

def manualMode() {
	log.debug "manualMode()"
    parent.setRoomManualMode(device.deviceNetworkId, true)
}