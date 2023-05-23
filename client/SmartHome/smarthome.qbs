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
        "room.cpp",
        "room.h",
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
            "pictures/add.png",
            "pictures/load.gif",
            "pictures/rooms/living_room.jpg",
            "pictures/rooms/bedroom.jpg",
            "pictures/rooms/lobby.jpg",
            "pictures/rooms/kitchen.jpg",
            "pictures/rooms/pantry.jpg",
            "pictures/rooms/bathroom.jpg",
            "pictures/rooms/toilet.jpg",
            "pictures/rooms/garage.jpg",
            "pictures/rooms/unknown.png"
        ]
    }
}
