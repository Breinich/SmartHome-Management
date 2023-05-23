import QtQuick
import QtQuick.Controls 6.2

Page {
    id: roomsview
    Dialog {
        id: createRoomDialog
        width: parent.width
        y: 50
        title: "Create room"
        modal: true
        standardButtons: Dialog.Apply | Dialog.Cancel
        closePolicy: Popup.NoAutoClose

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

        onApplied: {
            var bErr = false;
            textNewRoomSelectPhoto.color = "black";
            textNewRoomNameErr.visible = false;

            if(listViewAddRoom.currentIndex === -1)
            {
                textNewRoomSelectPhoto.color = "red";
                bErr = true;
            }

            if(inputNewRoomName.text === "")
            {
                textNewRoomNameErr.text = "Can not create room with empty name.";
                textNewRoomNameErr.visible = true;
                bErr = true;
            }

            if(!textNewRoomNameErr.visible)
            {
                var bTaken = false;

                for(var i = 0; i < listViewRoomsModel.count; i++)
                {
                    bTaken |= listViewRoomsModel.get(i).name1 === inputNewRoomName.text;
                    bTaken |= listViewRoomsModel.get(i).name2 === inputNewRoomName.text;
                    if(bTaken) break;
                }

                if(bTaken)
                {
                    textNewRoomNameErr.text = "Name already used. Room name must be unique.";
                    textNewRoomNameErr.visible = true;
                    bErr = true;
                }
            }

            if(!bErr)
            {
                //TODO: communicate with server
                createRoomDialog.accept();
            }
        }

        onRejected: {

        }
    }

    ListView {
        id: listViewRooms
        objectName: "ObjectListViewRooms"
        x: 0
        y: 10
        width: 640
        height: 440
        delegate: Item {
            x: 80
            width: 620
            height: 120
            Row {
                spacing: 10
                Image {
                    width: 120
                    height: 110
                    source: imgsource1
                    smooth: true
                    property int roomid: roomid1
                    visible: visible1
                    MouseArea {
                        anchors.fill: parent
                        onClicked: {
                            listViewRooms.roomClicked(roomid1);
                        }
                    }

                }

                Text {
                    text: name1
                    anchors.verticalCenter: parent.verticalCenter
                    font.bold: true
                    width: 150
                }

                Image {
                    width: 120
                    height: 110
                    source: imgsource2
                    smooth: true
                    property int roomid: roomid2
                    visible: visible2
                    MouseArea {
                        anchors.fill: parent
                        onClicked: {
                            listViewRooms.roomClicked(roomid2);
                        }
                    }

                }

                Text {
                    text: name2
                    anchors.verticalCenter: parent.verticalCenter
                    font.bold: true
                    width: 100
                }
            }
        }
        model: ListModel {
            id: listViewRoomsModel
            /*ListElement {
                name1: "Living Room"
                visible1: true
                roomid1: 3
                imgsource1: "living_room.jpg"
                name2: "Parent's Bedoom"
                visible2: true
                roomid2: 4
                imgsource2: "bedroom.jpg"
            }
            ListElement {
                name1: "Kitchen"
                visible1: true
                roomid1: 5
                imgsource1: "kitchen.jpg"
                name2: "Pantry"
                visible2: true
                roomid2: 6
                imgsource2: "pantry.jpg"
            }
            ListElement {
                name1: "Lobby"
                visible1: true
                roomid1: 7
                imgsource1: "lobby.jpg"
                name2: "Garage"
                visible2: true
                roomid2: 8
                imgsource2: "garage.jpg"
            }
            ListElement {
                name1: "Bathroom"
                visible1: true
                roomid1: 9
                imgsource1: "bathroom.jpg"
                name2: "Toilet"
                visible2: true
                roomid2: 10
                imgsource2: "toilet.jpg"
            }
            ListElement {
                name1: "Add room"
                visible1: true
                roomid1: -1
                imgsource1: "add.png"
                name2: ""
                visible2: false
                roomid2: -1
                imgsource2: "add.png"
            }*/
        }
        Component.onCompleted: {
            listViewRoomsModel.clear();
            httpcommunication.getRooms();
            /*listViewRoomsModel.append({"name1":"Living Room", "visible1":true, "roomid1":3, "imgsource1":"living_room.jpg",
                                      "name2":"Add room", "visible2":true, "roomid2":-1, "imgsource2":"add.png"});*/
        }


        function roomClicked(roomId) {
            if (roomId === -1)
            {
                listViewAddRoom.currentIndex = -1;
                inputNewRoomName.text = "";
                textNewRoomSelectPhoto.color = "black";
                textNewRoomNameErr.visible = false;
                createRoomDialog.open();
            }
            else{
                //TODO: open selected room view
            }
        }

        Connections{
            target: httpcommunication

            function onAddRoomsListItem(id1, name1, image1, bVisible1, id2, name2, image2, bVisible2) {
                listViewRoomsModel.append({"name1":name1, "visible1":bVisible1, "roomid1":id1, "imgsource1":image1,
                                          "name2":name2, "visible2":bVisible2, "roomid2":id2, "imgsource2":image2});
            }
        }
    }
}
