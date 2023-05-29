import QtQuick
import QtQuick.Controls 6.2

Page {
    id: statisticsview
    Label{
        text: "TODO: statisticsview"
    }

    Rectangle
    {
        id: actuatorItemRect
        y: 10
        x: 10
        width: 300
        height: 100
        property int actuatorId: listViewActuatorId
        color: "transparent"
        Column{
            x:20
            y:20
            Label {
                id: listViewActuatorsLabel
                width: 280
                height: 40
                text: listViewActuatorsName
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
                visible: listViewActuatorsVisibility

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
                    //TODO: post
                }
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
        }

        MouseArea {
            anchors.fill: parent
            hoverEnabled: true
            onEntered: {
                actuatorItemRect.color = "lightgrey";
                buttonEditActuator.visible = true;
                buttonDeleteActuator.visible = true;
            }
            onExited: {
                if(visible2)
                {
                    actuatorItemRect.color = "transparent";
                    buttonEditActuator.visible = false;
                    buttonDeleteActuator.visible = false;
                }
            }
            onClicked: {
                listViewRooms.roomClicked(roomid2);
            }
        }
    }
}
