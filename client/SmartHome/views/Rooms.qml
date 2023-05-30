import QtQuick
import QtQuick.Controls 6.2

Page {
    id: roomsview
    Dialog {
        id: confirmDeleteDialog
        anchors.centerIn: parent
        property int roomId : -1
        title: "Delete room"
        modal: true
        standardButtons: Dialog.Apply | Dialog.Cancel
        closePolicy: Popup.NoAutoClose
        contentItem: Text{
            padding: 20
        }
        onApplied: {
            httpcommunication.deleteRoom(roomId);
            confirmDeleteDialog.accept();
        }
    }

    Dialog {
        id: createRoomDialog
        width: parent.width
        y: 50
        title: ""
        property string createTitle : "Create room"
        property string editTitle: "Edit room"
        modal: true
        standardButtons: Dialog.Apply | Dialog.Cancel
        closePolicy: Popup.NoAutoClose
        property int roomId: -1

        contentItem: Column{
            Text{
                id: textNewRoomSelectPhoto
                text: "Select photo to the new room:"
                bottomPadding: 10
            }
            width: parent.width
            ListView {
                id: listViewAddRoom
                width: parent.width
                height: 132
                orientation: ListView.Horizontal
                currentIndex: -1
                ScrollBar.horizontal: ScrollBar{ }
                delegate: Rectangle
                {
                    id: listViewAddRoomDelegateRect
                    width: 124
                    height: 114
                    color: "transparent"
                    border.width: 2
                    border.color: ListView.isCurrentItem ? "#0f97dd" : "transparent"

                    Image {
                        id: listViewAddRoomDelegateImage
                        x: 2
                        y: 2
                        width: 120
                        height: 110
                        source: imgsource
                        smooth: true
                    }
                    MouseArea {
                        anchors.fill: parent
                        onClicked: {
                            listViewAddRoom.currentIndex = index;
                        }
                    }
                }

                model: ListModel {
                    id: listModelAddRoom
                    ListElement {
                        imgsource: "bedroom.jpg"
                    }

                    ListElement {
                        imgsource: "living_room.jpg"
                    }

                    ListElement {
                        imgsource: "kitchen.jpg"
                    }

                    ListElement {
                        imgsource: "bathroom.jpg"
                    }

                    ListElement {
                        imgsource: "toilet.jpg"
                    }

                    ListElement {
                        imgsource: "lobby.jpg"
                    }

                    ListElement {
                        imgsource: "garage.jpg"
                    }

                    ListElement {
                        imgsource: "pantry.jpg"
                    }
                }
            }

            Text{
                text: "Type name of the new room:"
                bottomPadding: 10
                topPadding: 10
            }
            Row{

                Rectangle {
                    id: rectNewRoomName
                    width: 220
                    height: 28
                    color: "#00ff0000"
                    border.width: 2
                    border.color: "black"
                    TextInput {
                        id: inputNewRoomName
                        x: 5
                        y: 0
                        width: 210
                        height: 28
                        font.pixelSize: 12
                        horizontalAlignment: Text.AlignLeft
                        verticalAlignment: Text.AlignVCenter
                        cursorVisible: false
                        activeFocusOnPress: true
                        clip: true
                    }
                }

                Text{
                    id: textNewRoomNameErr
                    padding: 5
                    color: "#ff0000"
                    visible: false
                }

            }
        }

        function clearError() {
            textNewRoomSelectPhoto.color = "black";
            textNewRoomNameErr.visible = false;
            rectNewRoomName.border.color = "black";
        }

        onApplied: {
            var bErr = false;
            createRoomDialog.clearError();
            var bCreate = (createRoomDialog.title === createRoomDialog.createTitle);

            if(listViewAddRoom.currentIndex === -1)
            {
                textNewRoomSelectPhoto.color = "red";
                bErr = true;
            }

            if(inputNewRoomName.text === "")
            {
                textNewRoomNameErr.text = "Can not create room with empty name.";
                textNewRoomNameErr.visible = true;
                rectNewRoomName.border.color = "red";
                bErr = true;
            }

            if(!textNewRoomNameErr.visible)
            {
                if(!userinputvalidator.validateEntityName(inputNewRoomName.text))
                {
                    textNewRoomNameErr.text = "Room name can only contain alphabetic characters, numbers and spaces.";
                    textNewRoomNameErr.visible = true;
                    rectNewRoomName.border.color = "red";
                    bErr = true;
                }
            }

            if(!textNewRoomNameErr.visible)
            {
                var bTaken = false;

                for(var i = 0; i < listViewRoomsModel.count; i++)
                {
                    if(bCreate)
                    {
                        bTaken |= listViewRoomsModel.get(i).name1 === inputNewRoomName.text;
                        bTaken |= listViewRoomsModel.get(i).name2 === inputNewRoomName.text;
                    }
                    else
                    {
                        if(listViewRoomsModel.get(i).roomid1 !== createRoomDialog.roomId)
                        {
                            bTaken |= listViewRoomsModel.get(i).name1 === inputNewRoomName.text;
                        }
                        if(listViewRoomsModel.get(i).roomid2 !== createRoomDialog.roomId)
                        {
                            bTaken |= listViewRoomsModel.get(i).name2 === inputNewRoomName.text;
                        }
                    }

                    if(bTaken) break;
                }

                if(bTaken)
                {
                    textNewRoomNameErr.text = "Name already used. Room name must be unique.";
                    textNewRoomNameErr.visible = true;
                    rectNewRoomName.border.color = "red";
                    bErr = true;
                }
            }

            if(!bErr)
            {
                if(bCreate)
                {
                    httpcommunication.createRoom(inputNewRoomName.text, listModelAddRoom.get(listViewAddRoom.currentIndex).imgsource);
                }
                else
                {
                    httpcommunication.updateRoom(createRoomDialog.roomId, inputNewRoomName.text, listModelAddRoom.get(listViewAddRoom.currentIndex).imgsource);
                }

                createRoomDialog.accept();
            }
        }
    }

    ListView {
        id: listViewRooms
        x: 0
        y: 10
        width: 640
        height: 440
        delegate: Item {
            x: 20
            width: 600
            height: 120
            Rectangle{
                id: listViewRoomsDelegateRect1
                x: 0
                y: 0
                width: parent.width / 2
                height: parent.height
                color: "transparent"
                property int roomid: roomid1
                Row{
                    x: 5
                    y: 5
                    spacing: 10
                    Image {
                        width: 120
                        height: 110
                        source: imgsource1
                        smooth: true
                        visible: visible1
                    }

                    Text {
                        text: name1
                        anchors.verticalCenter: parent.verticalCenter
                        font.bold: true
                        width: 160
                    }
                }
                MouseArea {
                    anchors.fill: parent
                    hoverEnabled: true
                    onEntered: {
                        if(visible1)
                        {
                            listViewRoomsDelegateRect1.color = "lightgrey";
                            if(imgsource1 !== "add.png")
                            {
                                buttonEdit1.visible = true;
                                buttonDelete1.visible = true;
                            }
                        }
                    }
                    onExited: {
                        if(visible1)
                        {
                            listViewRoomsDelegateRect1.color = "transparent";
                            if(imgsource1 !== "add.png")
                            {
                                buttonEdit1.visible = false;
                                buttonDelete1.visible = false;
                            }
                        }
                    }
                    onClicked: {
                        listViewRooms.roomClicked(roomid1);
                    }
                }
            }

            Rectangle{
                id: listViewRoomsDelegateRect2
                x: parent.width / 2
                y: 0
                width: parent.width / 2
                height: parent.height
                color: "transparent"
                property int roomid: roomid2
                Row{
                    x: 5
                    y: 5
                    spacing: 10
                    Image {
                        width: 120
                        height: 110
                        source: imgsource2
                        smooth: true
                        visible: visible2
                    }

                    Text {
                        text: name2
                        anchors.verticalCenter: parent.verticalCenter
                        font.bold: true
                        width: 160
                    }
                }
                MouseArea {
                    anchors.fill: parent
                    hoverEnabled: true
                    onEntered: {
                        if(visible2)
                        {
                            listViewRoomsDelegateRect2.color = "lightgrey";
                            if(imgsource2 !== "add.png")
                            {
                                buttonEdit2.visible = true;
                                buttonDelete2.visible = true;
                            }
                        }
                    }
                    onExited: {
                        if(visible2)
                        {
                            listViewRoomsDelegateRect2.color = "transparent";
                            if(imgsource2 !== "add.png")
                            {
                                buttonEdit2.visible = false;
                                buttonDelete2.visible = false;
                            }
                        }
                    }
                    onClicked: {
                        listViewRooms.roomClicked(roomid2);
                    }
                }
            }

            Button {
                id: buttonDelete1
                x: listViewRoomsDelegateRect1.x + listViewRoomsDelegateRect1.width - buttonDelete1.width - 5
                y: listViewRoomsDelegateRect1.y + listViewRoomsDelegateRect1.height - buttonDelete1.height - 5
                width: 60
                height: 25
                text: "Delete"
                visible: false
                hoverEnabled: false
                onClicked: {
                    confirmDeleteDialog.contentItem.text = qsTr("Do you want to delete %1?").arg(name1);
                    confirmDeleteDialog.roomId = roomid1;
                    confirmDeleteDialog.open();
                }
            }

            Button {
                id: buttonEdit1
                x: listViewRoomsDelegateRect1.x + listViewRoomsDelegateRect1.width - buttonDelete1.width - buttonEdit1.width - 10
                y: listViewRoomsDelegateRect1.y + listViewRoomsDelegateRect1.height - buttonEdit1.height - 5
                width: 60
                height: 25
                text: "Edit"
                visible: false
                hoverEnabled: false
                onClicked:{
                    var bFound = false;
                    var i=0;
                    for(; i < listModelAddRoom.count; i++)
                    {
                        if(listModelAddRoom.get(i).imgsource === imgsource1)
                        {
                            bFound = true;
                            break;
                        }
                    }

                    if(bFound)
                    {
                        listViewAddRoom.currentIndex = i;
                    }
                    else
                    {
                        listViewAddRoom.currentIndex = -1;
                    }

                    createRoomDialog.clearError();
                    inputNewRoomName.text = name1;
                    textNewRoomSelectPhoto.color = "black";
                    textNewRoomNameErr.visible = false;
                    createRoomDialog.roomId = roomid1;
                    createRoomDialog.title = createRoomDialog.editTitle;
                    createRoomDialog.open();
                }

            }

            Button {
                id: buttonDelete2
                x: listViewRoomsDelegateRect2.x + listViewRoomsDelegateRect2.width - buttonDelete2.width - 5
                y: listViewRoomsDelegateRect2.y + listViewRoomsDelegateRect2.height - buttonDelete2.height - 5
                width: 60
                height: 25
                text: "Delete"
                visible: false
                hoverEnabled: false
                onClicked: {
                    confirmDeleteDialog.contentItem.text = qsTr("Do you want to delete %1?").arg(name2);
                    confirmDeleteDialog.roomId = roomid2;
                    confirmDeleteDialog.open();
                }
            }

            Button {
                id: buttonEdit2
                x: listViewRoomsDelegateRect2.x + listViewRoomsDelegateRect2.width - buttonDelete2.width - buttonEdit2.width - 10
                y: listViewRoomsDelegateRect2.y + listViewRoomsDelegateRect2.height - buttonEdit2.height - 5
                width: 60
                height: 25
                text: "Edit"
                visible: false
                hoverEnabled: false
                onClicked:{
                    var bFound = false;
                    var i=0;
                    for(; i < listModelAddRoom.count; i++)
                    {
                        if(listModelAddRoom.get(i).imgsource === imgsource2)
                        {
                            bFound = true;
                            break;
                        }
                    }

                    if(bFound)
                    {
                        listViewAddRoom.currentIndex = i;
                    }
                    else
                    {
                        listViewAddRoom.currentIndex = -1;
                    }

                    createRoomDialog.clearError();
                    inputNewRoomName.text = name2;
                    textNewRoomSelectPhoto.color = "black";
                    textNewRoomNameErr.visible = false;
                    createRoomDialog.roomId = roomid2;
                    createRoomDialog.title = createRoomDialog.editTitle;
                    createRoomDialog.open();
                }
            }
        }

        model: ListModel {
            id: listViewRoomsModel
        }

        Component.onCompleted: {
            listViewRooms.refreshRooms();
        }

        function refreshRooms() {
            listViewRoomsModel.clear();
            httpcommunication.getRooms();
        }


        function roomClicked(roomId) {
            if (roomId === -1)
            {
                createRoomDialog.clearError();
                listViewAddRoom.currentIndex = -1
                inputNewRoomName.text = "";
                textNewRoomSelectPhoto.color = "black";
                textNewRoomNameErr.visible = false;
                createRoomDialog.roomId = -1;
                createRoomDialog.title = createRoomDialog.createTitle;
                createRoomDialog.open();
            }
            else{
                stack.showRoomView(roomId);
            }
        }

        Connections{
            target: httpcommunication

            function onAddRoomsListItem(id1, name1, image1, bVisible1, id2, name2, image2, bVisible2) {
                listViewRoomsModel.append({"name1":name1, "visible1":bVisible1, "roomid1":id1, "imgsource1":image1,
                                          "name2":name2, "visible2":bVisible2, "roomid2":id2, "imgsource2":image2});
            }

            function onRoomsUpdateNeeded() {
                listViewRooms.refreshRooms();
            }
        }
    }
}
