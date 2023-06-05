import QtQuick
import QtQuick.Controls 6.2

Page {
    id: loginview
    Label {
        id: labelEmail
        x: 120
        y: 252
        width: 69
        height: 28
        text: qsTr("E-Mail:")
        horizontalAlignment: Text.AlignLeft
        verticalAlignment: Text.AlignVCenter
        font.styleName: "Regular"
        font.family: "Arial"
        font.pointSize: 12
    }

    Dialog {
        id: dialogForgotPassword
        modal: true
        parent:  loginview
        x: parent.width/2 - dialogForgotPassword.width/2
        y: labelForgotPassword.y + dialogForgotPassword.height
        Label {
                text: qsTr("Please contact our support team") + ": <a href='mailto: support-smarthome@itcatcetc.com'>support-smarthome@itcatcetc.com</a>"
                textFormat: Text.RichText
                onLinkActivated: (link)=> {
                    Qt.openUrlExternally(link);
                }
        }
    }

    Rectangle {
        id: rectEmail
        x: 220
        y: 252
        width: 200
        height: 28
        color: "#00ff0000"
        border.color: "black"
        border.width: 2

        TextInput {
            id: inputEmail
            x: 5
            y: 0
            width: 190
            height: 28
            font.pixelSize: 12
            horizontalAlignment: Text.AlignLeft
            verticalAlignment: Text.AlignVCenter
            clip: true
            activeFocusOnPress: true
            cursorVisible: false
        }
    }

    Image {
        id: image
        x: 210
        y: 33
        width: 221
        height: 188
        source: "startpic.png"
        fillMode: Image.PreserveAspectFit
    }

    Label {
        id: labelPassword
        x: 119
        y: 298
        width: 70
        height: 28
        text: qsTr("Password:")
        verticalAlignment: Text.AlignVCenter
        font.family: "Arial"
        font.pointSize: 12
    }

    Rectangle {
        id: rectPassword
        x: 220
        y: 298
        width: 200
        height: 28
        color: "#00ffffff"
        border.color: "black"
        border.width: 2

        TextInput {
            id: inputPassword
            x: 5
            y: 0
            width: 190
            height: 28
            font.pixelSize: 12
            horizontalAlignment: Text.AlignLeft
            verticalAlignment: Text.AlignVCenter
            activeFocusOnPress: true
            cursorVisible: false
            echoMode: TextInput.Password
            passwordMaskDelay: 1000
            clip: true
        }

    }

    Text {
        id: textUserError
        x: 431
        y: 257
        width: 200
        height: 18
        color: "#ff0000"
        text: qsTr("Invalid E-Mail format")
        font.pixelSize: 12
        visible: false
    }



    Button {
        id: buttonLogin
        x: 276
        y: 357
        width: 88
        height: 24
        text: qsTr("Log in")
        focus: true
        font.pointSize: 12
        onClicked: {
            //Todo: communicate with server, check password
            textUserError.visible = false;
            rectEmail.border.color = "black";
            textPasswordError.visible = false;
            rectPassword.border.color = "black";
            if(!userinputvalidator.validateEmail(inputEmail.text))
            {
                textUserError.visible = true;
                rectEmail.border.color = "red";
            }

            if(inputPassword.text.length == 0)
            {
                textPasswordError.visible = true;
                rectPassword.border.color = "red";
            }

            if(!textUserError.visible && !textPasswordError.visible)
            {
                httpcommunication.login(inputEmail.text, inputPassword.text);
            }
        }
    }

    Label {
        id: labelForgotPassword
        x: 431
        y: 304
        text: "<a href='link_forgotpwd'>" + qsTr("Forgot password") + "</a>"
        verticalAlignment: Text.AlignVCenter
        textFormat: Text.RichText
        onLinkActivated: (link)=> {
            dialogForgotPassword.open();
        }
    }

    Label {
        id: label
        x: 235
        y: 403
        text: qsTr("No account yet?") + " <a href='link_registration'>" + qsTr("Create Account") + "</a>"
        textFormat: Text.RichText
        onLinkActivated: (link)=> {
                             stack.showRegistrationView();
                         }
    }

    Text {
        id: textPasswordError
        x: 220
        y: 333
        width: 200
        height: 18
        visible: false
        color: "#ff0000"
        text: qsTr("Password can not be empty")
        font.pixelSize: 12
        horizontalAlignment: Text.AlignHCenter
    }
}
