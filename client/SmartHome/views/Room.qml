import QtQuick
import QtQuick.Controls 6.2
import itcatcetc.ipaddress 1.0

Page {
    id: roomview
    IpAddress {
        id: ipaddress
    }

    Timer {
        id: updateSensorDataTimer
        interval: 10000
        repeat: true
        running: true
        onTriggered: {
            for(var i = 0; i < listViewSensorsModel.count; i++)
            {
                httpcommunication.getLatestDataBySensorId(listViewSensorsModel.get(i).listViewSensorsId);
            }
        }
    }

    Dialog {
        id: confirmDeleteItemDialog
        anchors.centerIn: parent
        property int itemId : -1
        property string titleForSensor: qsTr("Delete sensor")
        property string titleForActuator: qsTr("Delete actuator")
        title: "Delete room"
        modal: true
        standardButtons: Dialog.Apply | Dialog.Cancel
        closePolicy: Popup.NoAutoClose
        contentItem: Text{
            padding: 20
        }
        onApplied: {
            confirmDeleteItemDialog.title == titleForSensor ? httpcommunication.deleteSensor(confirmDeleteItemDialog.itemId) : httpcommunication.deleteActuator(confirmDeleteItemDialog.itemId)
            confirmDeleteItemDialog.accept();
        }
    }

    Dialog {
        id: createNewItemDialog
        anchors.centerIn: parent
        modal: true
        standardButtons: Dialog.Apply | Dialog.Cancel
        closePolicy: Popup.NoAutoClose
        property string titleForAddSensor: qsTr("Create sensor")
        property string titleForAddActuator: qsTr("Create actuator")
        property string titleForEditSensor: qsTr("Edit sensor")
        property string titleForEditActuator: qsTr("Edit actuator")
        property int itemId: -1

        contentItem: Column{
            x: 0
            y: 0
            padding: 10

            Text {
                width: 220
                bottomPadding: 10
                text: qsTr("Name: ")
                font.pointSize: 12
            }

            Rectangle {
                id: rectItemName
                width: 220
                height: 28
                color: "#00ff0000"
                border.color: "black"
                border.width: 2

                TextInput {
                    id: textInputItemName
                    x: 5
                    y: 0
                    width: 210
                    height: 28
                    font.pixelSize: 12
                    horizontalAlignment: Text.AlignLeft
                    verticalAlignment: Text.AlignVCenter
                    activeFocusOnPress: true
                    cursorVisible: false
                    clip: true
                }
            }

            Text{
                id: textItemNameError
                color: "red"
                visible: false
            }

            Text {
                width: 220
                topPadding: textItemNameError.visible ? 20 - textItemNameError.height : 20
                bottomPadding: 10
                text: qsTr("Type: ")
                font.pointSize: 12
            }

            ComboBox {
                id: comboBoxType
                width: 220
                height: 28
                font.pointSize: 12
                model: ["LIGHT", "TEMPERATURE"]
            }

            Text {
                width: 220
                topPadding: 20
                bottomPadding: 10
                text: qsTr("Address: ")
                font.pointSize: 12
            }

            Row{
                ComboBox {
                    id: comboBoxAddressProtocol
                    width: 50
                    height: 28
                    font.pointSize: 12
                    model: ["IPv4", "IPv6"]
                    onCurrentIndexChanged: {
                        if (currentIndex == 0){
                            rowIPv4.visible = true;
                            rowIpv6.visible = false;
                        }
                        else{
                            rowIPv4.visible = false;
                            rowIpv6.visible = true;
                        }
                    }
                }

                Row{
                    id: rowIPv4
                    x: 55
                    y: 0
                    leftPadding: 5
                    visible: true
                    Rectangle {
                        id: rectInputAddressIPv4_0
                        width: 30
                        height: 28
                        color: "#00ff0000"
                        border.color: "black"
                        border.width: 2

                        TextInput {
                            id: textInputAddressIPv4_0
                            x: 3
                            y: 0
                            width: 24
                            height: 28
                            font.pixelSize: 14
                            horizontalAlignment: Text.AlignRight
                            verticalAlignment: Text.AlignVCenter
                            clip: true
                            activeFocusOnPress: true
                            cursorVisible: false
                            inputMask: "000"
                        }
                    }

                    Text {
                        width: 5
                        height: 28
                        text: "."
                        font.pixelSize: 25
                        horizontalAlignment: Text.AlignHCenter
                        verticalAlignment: Text.AlignTop
                    }

                    Rectangle {
                        id: rectInputAddressIPv4_1
                        width: 30
                        height: 28
                        color: "#00ff0000"
                        border.color: "black"
                        border.width: 2

                        TextInput {
                            id: textInputAddressIPv4_1
                            x: 3
                            y: 0
                            width: 24
                            height: 28
                            font.pixelSize: 14
                            horizontalAlignment: Text.AlignRight
                            verticalAlignment: Text.AlignVCenter
                            clip: true
                            activeFocusOnPress: true
                            cursorVisible: false
                            inputMask: "000"
                        }
                    }

                    Text {
                        width: 5
                        height: 28
                        text: "."
                        font.pixelSize: 25
                        horizontalAlignment: Text.AlignHCenter
                        verticalAlignment: Text.AlignTop
                    }

                    Rectangle {
                        id: rectInputAddressIPv4_2
                        width: 30
                        height: 28
                        color: "#00ff0000"
                        border.color: "black"
                        border.width: 2

                        TextInput {
                            id: textInputAddressIPv4_2
                            x: 3
                            y: 0
                            width: 24
                            height: 28
                            font.pixelSize: 14
                            horizontalAlignment: Text.AlignRight
                            verticalAlignment: Text.AlignVCenter
                            clip: true
                            activeFocusOnPress: true
                            cursorVisible: false
                            inputMask: "000"
                        }
                    }

                    Text {
                        width: 5
                        height: 28
                        text: "."
                        font.pixelSize: 25
                        horizontalAlignment: Text.AlignHCenter
                        verticalAlignment: Text.AlignTop
                    }

                    Rectangle {
                        id: rectInputAddressIPv4_3
                        width: 30
                        height: 28
                        color: "#00ff0000"
                        border.color: "black"
                        border.width: 2

                        TextInput {
                            id: textInputAddressIPv4_3
                            x: 3
                            y: 0
                            width: 24
                            height: 28
                            font.pixelSize: 14
                            horizontalAlignment: Text.AlignRight
                            verticalAlignment: Text.AlignVCenter
                            clip: true
                            activeFocusOnPress: true
                            cursorVisible: false
                            inputMask: "000"
                        }
                    }
                }
                Row{
                    id: rowIpv6
                    x: 55
                    y: 0
                    leftPadding: 5
                    visible: false
                    Rectangle {
                        id: rectInputAddressIPv6_0
                        width: 36
                        height: 28
                        color: "#00ff0000"
                        border.color: "black"
                        border.width: 2

                        TextInput {
                            id: textInputAddressIPv6_0
                            x: 3
                            y: 0
                            width: 30
                            height: 28
                            font.pixelSize: 14
                            horizontalAlignment: Text.AlignRight
                            verticalAlignment: Text.AlignVCenter
                            clip: true
                            activeFocusOnPress: true
                            cursorVisible: false
                            inputMask: "hhhh"
                        }
                    }

                    Text {
                        width: 6
                        height: 28
                        text: ":"
                        font.pixelSize: 25
                        horizontalAlignment: Text.AlignHCenter
                        verticalAlignment: Text.AlignBottom
                    }

                    Rectangle {
                        id: rectInputAddressIPv6_1
                        width: 36
                        height: 28
                        color: "#00ff0000"
                        border.color: "black"
                        border.width: 2

                        TextInput {
                            id: textInputAddressIPv6_1
                            x: 3
                            y: 0
                            width: 30
                            height: 28
                            font.pixelSize: 14
                            horizontalAlignment: Text.AlignRight
                            verticalAlignment: Text.AlignVCenter
                            clip: true
                            activeFocusOnPress: true
                            cursorVisible: false
                            inputMask: "hhhh"
                        }
                    }

                    Text {
                        width: 6
                        height: 28
                        text: ":"
                        font.pixelSize: 25
                        horizontalAlignment: Text.AlignHCenter
                        verticalAlignment: Text.AlignBottom
                    }

                    Rectangle {
                        id: rectInputAddressIPv6_2
                        width: 36
                        height: 28
                        color: "#00ff0000"
                        border.color: "black"
                        border.width: 2

                        TextInput {
                            id: textInputAddressIPv6_2
                            x: 3
                            y: 0
                            width: 30
                            height: 28
                            font.pixelSize: 14
                            horizontalAlignment: Text.AlignRight
                            verticalAlignment: Text.AlignVCenter
                            clip: true
                            activeFocusOnPress: true
                            cursorVisible: false
                            inputMask: "hhhh"
                        }
                    }

                    Text {
                        width: 6
                        height: 28
                        text: ":"
                        font.pixelSize: 25
                        horizontalAlignment: Text.AlignHCenter
                        verticalAlignment: Text.AlignBottom
                    }

                    Rectangle {
                        id: rectInputAddressIPv6_3
                        width: 36
                        height: 28
                        color: "#00ff0000"
                        border.color: "black"
                        border.width: 2

                        TextInput {
                            id: textInputAddressIPv6_3
                            x: 3
                            y: 0
                            width: 30
                            height: 28
                            font.pixelSize: 14
                            horizontalAlignment: Text.AlignRight
                            verticalAlignment: Text.AlignVCenter
                            clip: true
                            activeFocusOnPress: true
                            cursorVisible: false
                            inputMask: "hhhh"
                        }
                    }

                    Text {
                        width: 6
                        height: 28
                        text: ":"
                        font.pixelSize: 25
                        horizontalAlignment: Text.AlignHCenter
                        verticalAlignment: Text.AlignBottom
                    }

                    Rectangle {
                        id: rectInputAddressIPv6_4
                        width: 36
                        height: 28
                        color: "#00ff0000"
                        border.color: "black"
                        border.width: 2

                        TextInput {
                            id: textInputAddressIPv6_4
                            x: 3
                            y: 0
                            width: 30
                            height: 28
                            font.pixelSize: 14
                            horizontalAlignment: Text.AlignRight
                            verticalAlignment: Text.AlignVCenter
                            clip: true
                            activeFocusOnPress: true
                            cursorVisible: false
                            inputMask: "hhhh"
                        }
                    }

                    Text {
                        width: 6
                        height: 28
                        text: ":"
                        font.pixelSize: 25
                        horizontalAlignment: Text.AlignHCenter
                        verticalAlignment: Text.AlignBottom
                    }

                    Rectangle {
                        id: rectInputAddressIPv6_5
                        width: 36
                        height: 28
                        color: "#00ff0000"
                        border.color: "black"
                        border.width: 2

                        TextInput {
                            id: textInputAddressIPv6_5
                            x: 3
                            y: 0
                            width: 30
                            height: 28
                            font.pixelSize: 14
                            horizontalAlignment: Text.AlignRight
                            verticalAlignment: Text.AlignVCenter
                            clip: true
                            activeFocusOnPress: true
                            cursorVisible: false
                            inputMask: "hhhh"
                        }
                    }

                    Text {
                        width: 6
                        height: 28
                        text: ":"
                        font.pixelSize: 25
                        horizontalAlignment: Text.AlignHCenter
                        verticalAlignment: Text.AlignBottom
                    }

                    Rectangle {
                        id: rectInputAddressIPv6_6
                        width: 36
                        height: 28
                        color: "#00ff0000"
                        border.color: "black"
                        border.width: 2

                        TextInput {
                            id: textInputAddressIPv6_6
                            x: 3
                            y: 0
                            width: 30
                            height: 28
                            font.pixelSize: 14
                            horizontalAlignment: Text.AlignRight
                            verticalAlignment: Text.AlignVCenter
                            clip: true
                            activeFocusOnPress: true
                            cursorVisible: false
                            inputMask: "hhhh"
                        }
                    }

                    Text {
                        width: 6
                        height: 28
                        text: ":"
                        font.pixelSize: 25
                        horizontalAlignment: Text.AlignHCenter
                        verticalAlignment: Text.AlignBottom
                    }

                    Rectangle {
                        id: rectInputAddressIPv6_7
                        width: 36
                        height: 28
                        color: "#00ff0000"
                        border.color: "black"
                        border.width: 2

                        TextInput {
                            id: textInputAddressIPv6_7
                            x: 3
                            y: 0
                            width: 30
                            height: 28
                            font.pixelSize: 14
                            horizontalAlignment: Text.AlignRight
                            verticalAlignment: Text.AlignVCenter
                            clip: true
                            activeFocusOnPress: true
                            cursorVisible: false
                            inputMask: "hhhh"
                        }
                    }
                }
            }
        }
        function clearInputs()
        {
            textInputItemName.text = ""
            comboBoxType.currentIndex = 0;
            comboBoxAddressProtocol.currentIndex = 0;
            textInputAddressIPv4_0.text = "";
            textInputAddressIPv4_1.text = "";
            textInputAddressIPv4_2.text = "";
            textInputAddressIPv4_3.text = "";
            textInputAddressIPv6_0.text = "";
            textInputAddressIPv6_1.text = "";
            textInputAddressIPv6_2.text = "";
            textInputAddressIPv6_3.text = "";
            textInputAddressIPv6_4.text = "";
            textInputAddressIPv6_5.text = "";
            textInputAddressIPv6_6.text = "";
            textInputAddressIPv6_7.text = "";
        }

        function clearErrors()
        {
            rectItemName.border.color = "black";
            textItemNameError.visible = false;
            rectInputAddressIPv4_0.border.color = "black";
            rectInputAddressIPv4_1.border.color = "black";
            rectInputAddressIPv4_2.border.color = "black";
            rectInputAddressIPv4_3.border.color = "black";
            rectInputAddressIPv6_0.border.color = "black";
            rectInputAddressIPv6_1.border.color = "black";
            rectInputAddressIPv6_2.border.color = "black";
            rectInputAddressIPv6_3.border.color = "black";
            rectInputAddressIPv6_4.border.color = "black";
            rectInputAddressIPv6_5.border.color = "black";
            rectInputAddressIPv6_6.border.color = "black";
            rectInputAddressIPv6_7.border.color = "black";
        }

        onApplied: {
            createNewItemDialog.clearErrors();
            var bErr = false;

            if(textInputItemName.text.length == 0)
            {
                textItemNameError.text = "Name can not be empty";
                textItemNameError.visible = true;
                rectItemName.border.color = "red";
                bErr = true;
            }

            if(!bErr)
            {
                if(!userinputvalidator.validateEntityName(textInputItemName.text))
                {
                    textItemNameError.text = "Name can only contain alphabetic characters, numbers and spaces.";
                    textItemNameError.visible = true;
                    rectItemName.border.color = "red";
                    bErr = true;
                }
            }

            if(!bErr)
            {
                var bTaken = false;
                var i;

                if(createNewItemDialog.title === titleForAddSensor)
                {
                    for(i = 0; i < listViewSensorsModel.count; i++)
                    {
                        bTaken |= listViewSensorsModel.get(i).listViewSensorsName === textInputItemName.text;
                        if(bTaken) break;
                    }

                }
                else if (createNewItemDialog.title === titleForEditSensor)
                {
                    for(i = 0; i < listViewSensorsModel.count; i++)
                    {
                        if(listViewSensorsModel.get(i).listViewSensorsId !== createNewItemDialog.itemId)
                        {
                            bTaken |= listViewSensorsModel.get(i).listViewSensorsName === textInputItemName.text;
                        }
                        if(bTaken) break;
                    }
                }
                else if (createNewItemDialog.title === titleForAddActuator)
                {
                    for(i = 0; i < listViewActuatorsModel.count; i++)
                    {
                        bTaken |= listViewActuatorsModel.get(i).listViewActuatorsName === textInputItemName.text;
                        if(bTaken) break;
                    }
                }
                else if (createNewItemDialog.title === titleForEditActuator)
                {
                    for(i = 0; i < listViewActuatorsModel.count; i++)
                    {
                        if(listViewActuatorsModel.get(i).listViewActuatorId !== createNewItemDialog.itemId)
                        {
                            bTaken |= listViewActuatorsModel.get(i).listViewActuatorsName === textInputItemName.text;
                        }
                        if(bTaken) break;
                    }
                }

                if(bTaken)
                {
                    textItemNameError.text = "Name must be unique.";
                    textItemNameError.visible = true;
                    rectItemName.border.color = "red";
                    bErr = true;
                }
            }

            if(comboBoxAddressProtocol.currentIndex === 0)
            {
                if(textInputAddressIPv4_0.text.length === 0)
                {
                    rectInputAddressIPv4_0.border.color = "red";
                    bErr = true;
                }
                if(textInputAddressIPv4_1.text.length === 0)
                {
                    rectInputAddressIPv4_1.border.color = "red";
                    bErr = true;
                }
                if(textInputAddressIPv4_2.text.length === 0)
                {
                    rectInputAddressIPv4_2.border.color = "red";
                    bErr = true;
                }
                if(textInputAddressIPv4_3.text.length === 0)
                {
                    rectInputAddressIPv4_3.border.color = "red";
                    bErr = true;
                }
            }
            else
            {
                if(textInputAddressIPv6_0.text.length === 0)
                {
                    rectInputAddressIPv6_0.border.color = "red";
                    bErr = true;
                }
                if(textInputAddressIPv6_1.text.length === 0)
                {
                    rectInputAddressIPv6_1.border.color = "red";
                    bErr = true;
                }
                if(textInputAddressIPv6_2.text.length === 0)
                {
                    rectInputAddressIPv6_2.border.color = "red";
                    bErr = true;
                }
                if(textInputAddressIPv6_3.text.length === 0)
                {
                    rectInputAddressIPv6_3.border.color = "red";
                    bErr = true;
                }
                if(textInputAddressIPv6_4.text.length === 0)
                {
                    rectInputAddressIPv6_4.border.color = "red";
                    bErr = true;
                }
                if(textInputAddressIPv6_5.text.length === 0)
                {
                    rectInputAddressIPv6_5.border.color = "red";
                    bErr = true;
                }
                if(textInputAddressIPv6_6.text.length === 0)
                {
                    rectInputAddressIPv6_6.border.color = "red";
                    bErr = true;
                }
                if(textInputAddressIPv6_7.text.length === 0)
                {
                    rectInputAddressIPv6_7.border.color = "red";
                    bErr = true;
                }
            }

            if(!bErr)
            {
                //TODO server
                var address = "";
                if(comboBoxAddressProtocol.currentIndex === 0)
                {
                    address = textInputAddressIPv4_0.text + "." + textInputAddressIPv4_1.text + "." + textInputAddressIPv4_2.text + "." + textInputAddressIPv4_3.text;
                }
                else
                {
                    address = textInputAddressIPv6_0.text + ":" + textInputAddressIPv6_1.text + ":" + textInputAddressIPv6_2.text + ":" + textInputAddressIPv6_3.text + ":" + textInputAddressIPv6_4.text + ":" + textInputAddressIPv6_5.text + ":" + textInputAddressIPv6_6.text + ":" + textInputAddressIPv6_7.text;
                }

                if(createNewItemDialog.title === createNewItemDialog.titleForAddSensor)
                {
                    httpcommunication.createSensor(textInputItemName.text, comboBoxType.currentText, address, stack.selectedRoomId);
                }
                else if(createNewItemDialog.title === createNewItemDialog.titleForAddActuator)
                {
                    httpcommunication.createActuator(textInputItemName.text, comboBoxType.currentText, address, stack.selectedRoomId);
                }
                else if(createNewItemDialog.title === createNewItemDialog.titleForEditSensor)
                {
                    httpcommunication.updateSensor(createNewItemDialog.itemId, textInputItemName.text, comboBoxType.currentText, address, stack.selectedRoomId);
                }
                else if(createNewItemDialog.title === createNewItemDialog.titleForEditActuator)
                {
                    httpcommunication.updateActuator(createNewItemDialog.itemId, textInputItemName.text, comboBoxType.currentText, address, stack.selectedRoomId);
                }
                createNewItemDialog.accept();
            }

        }
    }

    Rectangle {
        x: 0
        y: 0
        z: 100
        width: 320
        height: 25
        Text{
            x: 0
            y: 0
            width: parent.width / 2
            height: parent.height
            text: "Sensors:"
            verticalAlignment: Text.AlignVCenter
            leftPadding: 10
            font.pointSize: 12
        }
        Label{
            x: parent.width / 2
            y: 0
            width: parent.width / 2
            height: parent.height
            textFormat: Text.RichText
            text: "<a href='link_addsensor'>" + qsTr("Add new sensor") + "</a>"
            horizontalAlignment: Text.AlignLeft
            verticalAlignment: Text.AlignVCenter
            font.pointSize: 12
            onLinkActivated: (link)=> {
                createNewItemDialog.title = createNewItemDialog.titleForAddSensor
                createNewItemDialog.clearInputs();
                createNewItemDialog.clearErrors();
                createNewItemDialog.itemId = -1;
                createNewItemDialog.open();
            }

        }
    }

    Rectangle {
        x: 320
        y: 0
        z: 100
        width: 320
        height: 25
        Text{
            x: 0
            y: 0
            width: parent.width
            height: parent.height
            text: "Actuators:"
            verticalAlignment: Text.AlignVCenter
            leftPadding: 10
            font.pointSize: 12
        }
        Label{
            x: parent.width / 2
            y: 0
            width: parent.width / 2
            height: parent.height
            textFormat: Text.RichText
            text: "<a href='link_addactuator'>" + qsTr("Add new actuator") + "</a>"
            horizontalAlignment: Text.AlignLeft
            verticalAlignment: Text.AlignVCenter
            font.pointSize: 12
            onLinkActivated: (link)=> {
                createNewItemDialog.title = createNewItemDialog.titleForAddActuator;
                createNewItemDialog.clearInputs();
                createNewItemDialog.clearErrors();
                createNewItemDialog.itemId = -1;
                createNewItemDialog.open();
            }
        }
    }

    ListView {
        id: listViewSensors
        x: 0
        y: 25
        width: 320
        height: 415
        delegate:     Rectangle
        {
            id: sensorItemRect
            y: 10
            x: 10
            width: 300
            height: 100
            property int sensorId: listViewSensorsId
            property string address: listViewSensorsAddress
            property string sensorType: listViewSensorsType
            property bool valueInitialized: listViewSensorsValueInitialized
            color: "transparent"
            Row {
                x: 10
                y: 10

                Rectangle{
                    x: 0
                    y: 0
                    width: 80
                    height: 80
                    color: "transparent"
                    visible: listViewSensorsValueInitialized
                    Rectangle {
                        id: listViewSensorsRect
                        width: 80
                        height: 80
                        color: listViewSensorsType === "LIGHT" ? "yellow" : "transparent"
                        opacity: listViewSensorsValue * 0.01
                    }
                    Text {
                        id: listViewSensorsText
                        x: 0
                        y: 0
                        width: 80
                        height: 80
                        text: listViewSensorsType === "LIGHT" ? "" : (listViewSensorsValue + " °C")
                        font.pixelSize: 26
                        horizontalAlignment: Text.AlignHCenter
                        verticalAlignment: Text.AlignVCenter
                    }
                    Image {
                        id: listViewSensorsImage
                        x: 0
                        y: 0
                        width: 80
                        height: 80
                        source: (listViewSensorsType === "LIGHT" && listViewSensorsValue > 0) ? "lamp_on.png" : "lamp_off.png"
                        fillMode: Image.PreserveAspectFit
                        visible: listViewSensorsType === "LIGHT" ? true : false
                    }
                }

                Label {
                    id: listViewSensorsLabel
                    width: 200
                    height: 80
                    text: listViewSensorsName
                    horizontalAlignment: Text.AlignLeft
                    verticalAlignment: Text.AlignVCenter
                    font.pointSize: 12
                    leftPadding: 10
                }
            }

            Button {
                id: buttonDeleteSensor
                width: 60
                height: 25
                text: "Delete"
                hoverEnabled: false
                x: sensorItemRect.width - buttonDeleteSensor.width - 5
                y: 5
                onClicked: {
                    confirmDeleteItemDialog.title = confirmDeleteItemDialog.titleForSensor;
                    confirmDeleteItemDialog.itemId = listViewSensorsId;
                    confirmDeleteItemDialog.contentItem.text = qsTr("Do you want to delete %1?").arg(listViewSensorsName);
                    confirmDeleteItemDialog.open();
                }
            }

            Button {
                id: buttonEditSensor
                width: 60
                height: 25
                text: "Edit"
                hoverEnabled: false
                x: sensorItemRect.width - buttonEditSensor.width - buttonDeleteSensor.width - 10;
                y: 5
                onClicked: {
                    createNewItemDialog.clearInputs();
                    createNewItemDialog.clearErrors();
                    createNewItemDialog.title = createNewItemDialog.titleForEditSensor;
                    createNewItemDialog.itemId = listViewSensorsId;
                    textInputItemName.text = listViewSensorsName;
                    var bFound = false;
                    for(var i=0; i < comboBoxType.count; i++)
                    {
                        if(comboBoxType.textAt(i) === listViewSensorsType)
                        {
                            comboBoxType.currentIndex = i;
                            bFound = true;
                            break;
                        }
                    }
                    if(!bFound)
                    {
                        comboBoxType.currentIndex = -1;
                    }

                    bFound = false;
                    if(ipaddress.isIPv4(listViewSensorsAddress))
                    {
                        comboBoxAddressProtocol.currentIndex = 0;
                        textInputAddressIPv4_0.text = ipaddress.getPartAt(0, listViewSensorsAddress);
                        textInputAddressIPv4_1.text = ipaddress.getPartAt(1, listViewSensorsAddress);
                        textInputAddressIPv4_2.text = ipaddress.getPartAt(2, listViewSensorsAddress);
                        textInputAddressIPv4_3.text = ipaddress.getPartAt(3, listViewSensorsAddress);
                        bFound = true
                    }

                    if(ipaddress.isIPv6(listViewSensorsAddress))
                    {
                        comboBoxAddressProtocol.currentIndex = 1;
                        textInputAddressIPv6_0.text = ipaddress.getPartAt(0, listViewSensorsAddress);
                        textInputAddressIPv6_1.text = ipaddress.getPartAt(1, listViewSensorsAddress);
                        textInputAddressIPv6_2.text = ipaddress.getPartAt(2, listViewSensorsAddress);
                        textInputAddressIPv6_3.text = ipaddress.getPartAt(3, listViewSensorsAddress);
                        textInputAddressIPv6_4.text = ipaddress.getPartAt(4, listViewSensorsAddress);
                        textInputAddressIPv6_5.text = ipaddress.getPartAt(5, listViewSensorsAddress);
                        textInputAddressIPv6_6.text = ipaddress.getPartAt(6, listViewSensorsAddress);
                        textInputAddressIPv6_7.text = ipaddress.getPartAt(7, listViewSensorsAddress);
                        bFound = true
                    }

                    if(!bFound)
                    {
                        comboBoxAddressProtocol.currentIndex = -1;
                    }

                    createNewItemDialog.open();
                }
            }
        }
        model: ListModel {
            id: listViewSensorsModel
        }
        ScrollBar.vertical: ScrollBar {}
        function refreshSensors()
        {
            listViewSensorsModel.clear();
            httpcommunication.getSensorsPerRoom(stack.selectedRoomId);
        }
    }

    ListView {
        id: listViewActuators
        x: 320
        y: 25
        width: 320
        height: 440
        delegate: Rectangle
        {
            id: actuatorItemRect
            y: 10
            x: 0
            width: 310
            height: 120
            property int actuatorId: listViewActuatorId
            property string actuatorType: listViewActuatorType
            property string actuatorAddress: listViewactuatorsAddress
            color: "transparent"
            Column{
                x:20
                y:20
                height: 100
                Label {
                    id: listViewActuatorsLabel
                    width: 280
                    height: 40
                    text: listViewActuatorsName + ": " + listViewActuatorsValue + (listViewActuatorType === "TEMPERATURE" ? "°C" : "%")
                    horizontalAlignment: Text.AlignLeft
                    verticalAlignment: Text.AlignVCenter
                    font.pointSize: 12
                }

                Slider {
                    id: actuatorSlider
                    width: 280
                    from: listViewActuatorsValueFrom
                    to: listViewActuatorsValueTo
                    value: listViewActuatorsValue
                    stepSize: 1

                    background: Rectangle {
                        x: actuatorSlider.leftPadding
                        y: actuatorSlider.topPadding + actuatorSlider.availableHeight / 2 - height / 2
                        implicitWidth: 200
                        implicitHeight: 4
                        width: actuatorSlider.availableWidth
                        height: implicitHeight
                        radius: 2
                        color: "#bdbebf"

                        Rectangle {
                            width: actuatorSlider.visualPosition * parent.width
                            height: parent.height
                            color: "#21be2b"
                            radius: 2
                        }
                    }

                    handle: Rectangle {
                        x: actuatorSlider.leftPadding + actuatorSlider.visualPosition * (actuatorSlider.availableWidth - width)
                        y: actuatorSlider.topPadding + actuatorSlider.availableHeight / 2 - height / 2
                        implicitWidth: 26
                        implicitHeight: 26
                        radius: 13
                        color: actuatorSlider.pressed ? "#f0f0f0" : "#f6f6f6"
                        border.color: "#bdbebf"
                    }

                    onMoved: {
                        listViewActuatorsLabel.text = listViewActuatorsName + ": " + actuatorSlider.value + (listViewActuatorType === "TEMPERATURE" ? "°C" : "%");
                    }
                }

                Text {
                    text: qsTr("Status: ") + listViewActuatorsOn
                    font.pointSize: 12
                }
            }

            Button {
                id: buttonDeleteActuator
                width: 60
                height: 25
                text: "Delete"
                visible: true
                hoverEnabled: false
                x: actuatorItemRect.width - buttonDeleteActuator.width - 5
                y: 5
                onClicked: {
                    confirmDeleteItemDialog.title = confirmDeleteItemDialog.titleForActuator;
                    confirmDeleteItemDialog.itemId = listViewActuatorId;
                    confirmDeleteItemDialog.contentItem.text = qsTr("Do you want to delete %1?").arg(listViewActuatorsName);
                    confirmDeleteItemDialog.open();
                }
            }

            Button {
                id: buttonEditActuator
                width: 60
                height: 25
                text: "Edit"
                visible: true
                hoverEnabled: false
                x: actuatorItemRect.width - buttonEditActuator.width - buttonDeleteActuator.width - 10;
                y: 5
                onClicked: {
                    createNewItemDialog.clearInputs();
                    createNewItemDialog.clearErrors();
                    createNewItemDialog.title = createNewItemDialog.titleForEditActuator;
                    createNewItemDialog.itemId = listViewActuatorId;
                    textInputItemName.text = listViewActuatorsName;
                    var bFound = false;
                    for(var i=0; i < comboBoxType.count; i++)
                    {
                        if(comboBoxType.textAt(i) === listViewActuatorType)
                        {
                            comboBoxType.currentIndex = i;
                            bFound = true;
                            break;
                        }
                    }
                    if(!bFound)
                    {
                        comboBoxType.currentIndex = -1;
                    }

                    bFound = false;
                    if(ipaddress.isIPv4(listViewactuatorsAddress))
                    {
                        comboBoxAddressProtocol.currentIndex = 0;
                        textInputAddressIPv4_0.text = ipaddress.getPartAt(0, listViewactuatorsAddress);
                        textInputAddressIPv4_1.text = ipaddress.getPartAt(1, listViewactuatorsAddress);
                        textInputAddressIPv4_2.text = ipaddress.getPartAt(2, listViewactuatorsAddress);
                        textInputAddressIPv4_3.text = ipaddress.getPartAt(3, listViewactuatorsAddress);
                        bFound = true
                    }

                    if(ipaddress.isIPv6(listViewactuatorsAddress))
                    {
                        comboBoxAddressProtocol.currentIndex = 1;
                        textInputAddressIPv6_0.text = ipaddress.getPartAt(0, listViewactuatorsAddress);
                        textInputAddressIPv6_1.text = ipaddress.getPartAt(1, listViewactuatorsAddress);
                        textInputAddressIPv6_2.text = ipaddress.getPartAt(2, listViewactuatorsAddress);
                        textInputAddressIPv6_3.text = ipaddress.getPartAt(3, listViewactuatorsAddress);
                        textInputAddressIPv6_4.text = ipaddress.getPartAt(4, listViewactuatorsAddress);
                        textInputAddressIPv6_5.text = ipaddress.getPartAt(5, listViewactuatorsAddress);
                        textInputAddressIPv6_6.text = ipaddress.getPartAt(6, listViewactuatorsAddress);
                        textInputAddressIPv6_7.text = ipaddress.getPartAt(7, listViewactuatorsAddress);
                        bFound = true
                    }

                    if(!bFound)
                    {
                        comboBoxAddressProtocol.currentIndex = -1;
                    }

                    createNewItemDialog.open();
                }
            }

            Button {
                id: buttonSendCommand
                width: 60
                height: 25
                text: "Set"
                visible: true
                hoverEnabled: false
                x: actuatorItemRect.width - buttonSendCommand.width - 5
                y: actuatorItemRect.height - 25
                onClicked: {
                    httpcommunication.sendCommand(listViewActuatorType, stack.selectedRoomId, actuatorSlider.value);
                }
            }
        }

        ScrollBar.vertical: ScrollBar {}

        model: ListModel {
            id: listViewActuatorsModel
        }

        function refreshActuators()
        {
            listViewActuatorsModel.clear();
            httpcommunication.getActuatorsPerRoom(stack.selectedRoomId);
        }

        Connections{
            target: httpcommunication

            function onAddSensorsPerRoomListItem(id, name, type, address) {
                listViewSensorsModel.append({listViewSensorsId: id, listViewSensorsType: type, listViewSensorsAddress: address, listViewSensorsName: name, listViewSensorsValueInitialized: false, listViewSensorsValue: 0});
            }

            function onAddActuatorsPerRoomListItem(id, name, type, address) {
                listViewActuatorsModel.append({listViewActuatorId: id, listViewActuatorsName: name, listViewActuatorType: type, listViewActuatorsValueFrom: type === "TEMPERATURE" ? -30 : 0, listViewActuatorsValueTo: 100, listViewActuatorsValue: 22, listViewactuatorsAddress: address, listViewActuatorsOn: "OFF"});
            }

            function onSensorsPerRoomUpdateNeeded() {
                listViewSensors.refreshSensors();
            }

            function onActuatorsPerRoomUpdateNeeded() {
                listViewActuators.refreshActuators();
            }

            function onUpdateSensorValue(sensorId, sensorValue)
            {
                for(var i = 0; i < listViewSensorsModel.count; i++)
                {
                    if(listViewSensorsModel.get(i).listViewSensorsId === sensorId)
                    {
                        var oldModelItem = listViewSensorsModel.get(i);
                        var oldSensorId = oldModelItem.listViewSensorsId;
                        var oldType = oldModelItem.listViewSensorsType;
                        var oldAddress = oldModelItem.listViewSensorsAddress;
                        var oldName = oldModelItem.listViewSensorsName;
                        listViewSensorsModel.remove(i);
                        listViewSensorsModel.insert(i, {listViewSensorsId: oldSensorId, listViewSensorsType: oldType, listViewSensorsAddress: oldAddress, listViewSensorsName: oldName, listViewSensorsValueInitialized: true, listViewSensorsValue: sensorValue})
                        break;
                    }
                }
                stack.setFooterMessage("");
            }

            function onCommandActivated () {
                stack.setFooterMessage("Command sent");
            }

        }
    }

    Component.onCompleted: {
        listViewSensors.refreshSensors();
        listViewActuators.refreshActuators();
    }
}
