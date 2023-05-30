import QtQuick
import QtQuick.Controls 6.2
import QtCharts 6.2

Page {
    id: statisticsview
    Column{
        anchors.centerIn: parent
        Row{
            Text {
                id: text1
                width: 90
                height: 65
                text: qsTr("Select sensor")
                font.pixelSize: 14
                horizontalAlignment: Text.AlignLeft
                verticalAlignment: Text.AlignVCenter
            }
            ComboBox {
                id: comboSensorName
                property bool bInitialized: false
                y: 18
                width: 135
                height: 30
                textRole: "sensorName"
                model: ListModel {
                        id: comboSensorNameModel
                    }
                Component.onCompleted: {
                    comboSensorNameModel.clear();
                    comboSensorName.bInitialized = false;
                    httpcommunication.getAllSensors();
                }
                onActivated: {
                    lineSeries.clear();
                    lineSeries.name = comboSensorName.currentText;
                    httpcommunication.getLastHourOfSensorData(comboSensorNameModel.get(comboSensorName.currentIndex).sensorId);
                }
            }
        }
        ChartView {
            id: chartView
            width: 600
            height: 350
            LineSeries {
                id: lineSeries
                name: comboSensorName.currentText
                axisX: ValueAxis {
                    id: timeAxis
                    min: -3600
                    max: 0
                }

                axisY: ValueAxis {
                    id: valueAxis
                    min: comboSensorNameModel.get(comboSensorName.currentIndex).sensorType === "LIGHT" ? 0 : - 30
                    max: 100
                    tickCount: 11
                }
            }
        }

    }

    Connections{
        target: httpcommunication

        function onAddSensor(id, name, type, address, roomId)
        {
            comboSensorNameModel.append({sensorId: id, sensorName: name, sensorType: type, sensorAddress: address, roomId: roomId});
            comboSensorName.currentIndex = 0;
            if(!comboSensorName.bInitialized)
            {
                httpcommunication.getLastHourOfSensorData(comboSensorNameModel.get(comboSensorName.currentIndex).sensorId);
                comboSensorName.bInitialized = true;
            }
        }

        function onAddSensorStatisticData(xValue, yValue)
        {
            lineSeries.append(xValue, yValue);
        }
    }
}
