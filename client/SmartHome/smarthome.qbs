import qbs
CppApplication {
    Depends { name: "Qt.quick" }
    install: true
    // Additional import path used to resolve QML modules in Qt Creator's code model
    property pathList qmlImportPaths: []

    files: [
        "inputvalidator.cpp",
        "inputvalidator.h",
        "main.cpp",
        "networkcommunication.cpp",
        "networkcommunication.h",
    ]

    Group {
        name: "Views"
        Qt.core.resourcePrefix: "SmartHome/"
        fileTags: ["qt.qml.qml", "qt.core.resource_data"]
        files: [
            "views/Lightening.qml",
            "views/Login.qml",
            "views/Main.qml",
            "views/Registration.qml",
            "views/Rooms.qml",
            "views/Statistics.qml",
            "views/Temperature.qml",
        ]
    }

    Group {
        name: "Pictures"
        Qt.core.resourcePrefix: "SmartHome/"
        fileTags: ["qt.core.resource_data"]
        files: [
            "pictures/startpic.png",
            "pictures/logo.png",
            "pictures/back.png",
            "pictures/info.png",
        ]
    }
}
