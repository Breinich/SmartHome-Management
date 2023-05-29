import QtQuick
import QtQuick.Controls 6.2

Page {
    id: registrationview
    visible: true

    Dialog {
        id: dialogPasswordReq
        modal: true
        padding: 20
        parent:  registrationview
        x: infoRoundButton.x-dialogPasswordReq.width + infoRoundButton.width / 2
        y: infoRoundButton.y-(dialogPasswordReq.height + 5)
        contentItem: Label {
                text: qsTr("Password must meet the following requirements:"+
                            "<ul>"+
                              "<li>at least 8 characters long</li>"+
                              "<li>must contain lowercase character</li>"+
                              "<li>must contain uppercase character</li>"+
                              "<li>must contain number</li>"+
                              "<li>must contain special character</li>"+
                            "</ul>")
                textFormat: Text.RichText
        }
    }

    Rectangle {
        id: rectEmailReg
        x: 244
        y: 210
        width: 220
        height: 28
        color: "#00ff0000"
        border.color: "black"
        border.width: 2

        TextInput {
            id: inputEmailReg
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

    Text {
        id: firstNameText
        x: 135
        y: 77
        width: 103
        height: 32
        text: qsTr("First Name")
        font.pixelSize: 12
        verticalAlignment: Text.AlignVCenter
    }

    Text {
        id: lastNameText
        x: 135
        y: 142
        width: 103
        height: 32
        text: qsTr("Last Name")
        font.pixelSize: 12
        verticalAlignment: Text.AlignVCenter
    }

    Text {
        id: emailText
        x: 135
        y: 210
        width: 103
        height: 28
        text: qsTr("E-Mail address")
        font.pixelSize: 12
        verticalAlignment: Text.AlignVCenter
    }

    Text {
        id: pwdText
        x: 135
        y: 275
        width: 103
        height: 32
        text: qsTr("Password")
        font.pixelSize: 12
        verticalAlignment: Text.AlignVCenter
    }

    Text {
        id: pwd2Text
        x: 135
        y: 339
        width: 103
        height: 32
        text: qsTr("Repeat password")
        font.pixelSize: 12
        verticalAlignment: Text.AlignVCenter
    }

    Button {
        id: buttonCreateAccount
        x: 291
        y: 403
        width: 127
        height: 25
        text: qsTr("Create account")
        onClicked: {
            textFirstNameErrReg.visible = false;
            rectFirstNameReg.border.color = "black";
            textLastNameErrReg.visible = false;
            rectLastNameReg.border.color = "black";
            textEmailErrReg.visible = false;
            rectEmailReg.border.color = "black";
            textPwdErrReg.visible = false;
            rectPwdReg.border.color = "black";
            textPwdRepeatErrReg.visible = false;
            rectPwdRepeatReg.border.color = "black";

            if(!userinputvalidator.validateName(inputFirstNameReg.text))
            {
                textFirstNameErrReg.visible = true;
                rectFirstNameReg.border.color = "red";
            }

            if(!userinputvalidator.validateName(inputLastNameReg.text))
            {
                textLastNameErrReg.visible = true;
                rectLastNameReg.border.color = "red";
            }

            if(!userinputvalidator.validateEmail(inputEmailReg.text))
            {
                textEmailErrReg.visible = true;
                rectEmailReg.border.color = "red";
            }

            if(!userinputvalidator.validatePassword(inputPwdReg.text))
            {
                textPwdErrReg.visible = true;
                rectPwdReg.border.color = "red";
            }
            else
            {
                if(inputPwdReg.text != inputPwdRepeatReg.text)
                {
                    textPwdRepeatErrReg.visible = true;
                    rectPwdRepeatReg.border.color = "red";
                }
            }

            if(!textFirstNameErrReg.visible && !textLastNameErrReg.visible && !textEmailErrReg.visible && !textPwdErrReg.visible && !textPwdRepeatErrReg.visible)
            {
                httpcommunication.createAccount(inputFirstNameReg.text, inputLastNameReg.text, inputEmailReg.text, inputPwdReg.text);
            }
        }
    }

    Button {
        id: buttonBack
        x: 550
        y: 19
        width: 68
        height: 54
        opacity: 1
        visible: true
        text: qsTr("")
        icon.color: "#000000"
        rightPadding: 0
        bottomPadding: 0
        leftPadding: 0
        topPadding: 0
        icon.width: 50
        icon.height: 60
        flat: false
        baselineOffset: 18
        rightInset: 0
        icon.source: "back.png"
        display: AbstractButton.IconOnly
        ToolTip.visible: hovered
        ToolTip.text: qsTr("Back")
        ToolTip.delay: 1000
        onClicked: {
            stack.showLoginView();
        }
    }

    Rectangle {
        id: rectFirstNameReg
        x: 244
        y: 81
        width: 220
        height: 28
        color: "#00ff0000"
        border.color: "black"
        border.width: 2
        TextInput {
            id: inputFirstNameReg
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

    Rectangle {
        id: rectLastNameReg
        x: 244
        y: 144
        width: 220
        height: 28
        color: "#00ff0000"
        border.color: "black"
        border.width: 2
        TextInput {
            id: inputLastNameReg
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

    Rectangle {
        id: rectPwdReg
        x: 244
        y: 279
        width: 220
        height: 28
        color: "#00ff0000"
        border.color: "black"
        border.width: 2
        TextInput {
            id: inputPwdReg
            x: 5
            y: 0
            width: 210
            height: 28
            font.pixelSize: 12
            horizontalAlignment: Text.AlignLeft
            verticalAlignment: Text.AlignVCenter
            cursorVisible: false
            activeFocusOnPress: true
            echoMode: TextInput.Password
            passwordMaskDelay: 1000
            clip: true
        }
    }

    Rectangle {
        id: rectPwdRepeatReg
        x: 244
        y: 343
        width: 220
        height: 28
        color: "#00ff0000"
        border.color: "black"
        border.width: 2
        TextInput {
            id: inputPwdRepeatReg
            x: 5
            y: 0
            width: 210
            height: 28
            font.pixelSize: 12
            horizontalAlignment: Text.AlignLeft
            verticalAlignment: Text.AlignVCenter
            cursorVisible: false
            activeFocusOnPress: true
            echoMode: TextInput.Password
            passwordMaskDelay: 1000
            clip: true
        }
    }

    RoundButton {
        id: infoRoundButton
        x: 477
        y: 279
        width: 28
        height: 28
        radius: 14
        text: ""
        spacing: 0
        padding: 0
        rightPadding: 0
        bottomPadding: 0
        leftPadding: 0
        topPadding: 0
        icon.source: "info.png"
        icon.height: height
        icon.width: width
        display: AbstractButton.IconOnly
        onClicked: {
            dialogPasswordReq.open();
        }
    }

    Text {
        id: textEmailErrReg
        visible: false
        x: 298
        y: 244
        width: 113
        height: 19
        color: "#ff0000"
        text: "Invalid E-Mail format"
        font.pixelSize: 12
        horizontalAlignment: Text.AlignLeft
        textFormat: Text.PlainText
    }

    Text {
        id: textPwdErrReg
        visible: false
        x: 207
        y: 317
        color: "#ff0000"
        text: qsTr("Please check password requirements by clicking <em> i </em>button")
        font.pixelSize: 12
        textFormat: Text.RichText
    }

    Text {
        id: textPwdRepeatErrReg
        visible: false
        x: 268
        y: 377
        color: "#ff0000"
        text: qsTr("The given passwords don't mach")
        font.pixelSize: 12
    }

    Text {
        id: textFirstNameErrReg
        visible: false
        x: 44
        y: 115
        color: "#ff0000"
        text: qsTr("First name can contain only uppercase and lowercase alphabetic characters. No whitespaces are allowed.")
        font.pixelSize: 12
    }

    Text {
        id: textLastNameErrReg
        visible: false
        x: 45
        y: 180
        color: "#ff0000"
        text: qsTr("Last name can contain only uppercase and lowercase alphabetic characters. No whitespaces are allowed.")
        font.pixelSize: 12
    }

}
