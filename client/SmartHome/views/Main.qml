import QtQuick
import QtQuick.Window
import QtQuick.Controls 6.2
import itcatcetc.networkcommunication 1.0
import itcatcetc.inputvalidator 1.0

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
        z: 100
        Menu {
            title: qsTr("Views");
            MenuItem {
                text: qsTr("Rooms");
                onTriggered: {
                    stack.showRoomsView();
                }
            }
            MenuItem {
                text: qsTr("Temperature");
                onTriggered: {
                    stack.showTemperatureView();
                }
            }
            MenuItem {
                text: qsTr("Lightening");
                onTriggered: {
                    stack.showLighteningView()
                }
            }
        }
        Menu {
            title: qsTr("Diagnose")
            MenuItem {
                text: qsTr("Statistics")
                onTriggered: {
                    stack.showStatisticsView();
                }
            }
        }
        Menu {
            title: qsTr("Profil")
            MenuItem {
                text: qsTr("Log out")
                onTriggered: {
                    httpcommunication.logout();
                }
            }
        }
    }
    footer: Label{
        id: footerLabel
        leftPadding: 5
        bottomPadding: 2
    }

    Rectangle {
        id: networkCommunicationProgress
        width: mainwindow.width
        height: mainwindow.height
        color: "white"
        visible: false
        z:11
        AnimatedImage {
            source: "load.gif"
            height: 100
            width: 100
            anchors.centerIn: parent
        }
    }

    StackView {
        id: stack
        z: 10
        anchors.fill: parent
        initialItem: "Login.qml"
        replaceEnter: null //don't need animation
        replaceExit: null

        Dialog{
            id: dialogNetworkError
            anchors.centerIn: parent
            modal: true
            padding: 20
            contentItem: Label {
                id: dialogNetworkErrorLabel
                color: "red"
            }
        }

        NetworkCommunication
        {
            id: httpcommunication
        }

        InputValidator
        {
            id: userinputvalidator
        }

        function showLoginView()
        {
            menuBar.visible = false;
            stack.setFooterMessage("");
            stack.replace("Login.qml");
        }

        function showRegistrationView()
        {
            menuBar.visible = false;
            stack.setFooterMessage("");
            stack.replace("Registration.qml");
        }

        function showRoomsView()
        {
            menuBar.visible = true;
            stack.setFooterMessage("");
            stack.replace("Rooms.qml");
        }

        function showTemperatureView()
        {
            menuBar.visible = true;
            stack.setFooterMessage("");
            stack.replace("Temperature.qml");
        }

        function showLighteningView()
        {
            menuBar.visible = true;
            stack.setFooterMessage("");
            stack.replace("Lightening.qml");
        }

        function showStatisticsView()
        {
            menuBar.visible = true;
            stack.setFooterMessage("");
            stack.replace("Statistics.qml");
        }

        function setFooterMessage(strMessage)
        {
            footerLabel.text = strMessage;
        }

        Connections {
            target: httpcommunication

            function onCommunicationStarted() {
                networkCommunicationProgress.visible = true;
            }

            function onCommunicationFinished() {
                networkCommunicationProgress.visible = false;
            }

            function onReportError(strError) {
                dialogNetworkErrorLabel.text = strError;
                dialogNetworkError.open();
            }

            function onAccountCreated() {
                stack.showLoginView();
                stack.setFooterMessage("Account created");
            }

            function onSuccessfulSignIn() {
                stack.showRoomsView();
            }

            function onSuccessfulLogOut() {
                stack.showLoginView();
            }
        }
    }
}
