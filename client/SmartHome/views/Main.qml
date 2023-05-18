import QtQuick
import QtQuick.Window
import QtQuick.Controls 6.2
import itcatcetc.networkcommunication 1.0

ApplicationWindow {
    minimumWidth: 640
    maximumWidth: 640
    minimumHeight: 480
    maximumHeight: 480
    visible: true
    title: qsTr("Smart Home")
    id: mainwindow

    menuBar: MenuBar {
        visible: false
        id: menuBar
        Menu {
            title: qsTr("Views");
            MenuItem {
                text: qsTr("Rooms");
                onTriggered: {
                    stack.replace("Rooms.qml");
                }
            }
            MenuItem {
                text: qsTr("Temperature");
                onTriggered: {
                    stack.replace("Temperature.qml");
                }
            }
            MenuItem {
                text: qsTr("Lightening");
                onTriggered: {
                    stack.replace("Lightening.qml");
                }
            }
        }
        Menu {
            title: qsTr("Diagnose")
            MenuItem {
                text: qsTr("Statistics")
                onTriggered: {
                    stack.replace("Statistics.qml");
                }
            }
            /*MenuItem {
                text: qsTr("Services")
            }*/
        }
        Menu {
            title: qsTr("Profil")
            /*MenuItem {
                text: qsTr("Change password")
            }*/
            MenuItem {
                text: qsTr("Log out")
                onTriggered: {
                    //Todo: communicate with server, invalidate Token
                    stack.hideMenu();
                    stack.replace("Login.qml");
                }
            }
        }
    }

    StackView {
        id: stack
        anchors.fill: parent
        initialItem: "Login.qml"
        replaceEnter: null //don't need animation
        replaceExit: null

        NetworkCommunication
        {
            id: httpcommunication
        }

        function showMenu(){
            menuBar.visible = true;
        }

        function hideMenu()
        {
            menuBar.visible = false;
        }
    }
}
