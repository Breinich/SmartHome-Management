#ifndef ROOM_H
#define ROOM_H
#include <QString>
#include <QList>

class Room
{
public:
    Room(int id, const QString& name, const QString& photo);
    int id() const;
    QString name() const;
    QString photo() const;

    static int getAddRoomId();
    static QString getAddRoomName();
    static QString getAddRoomPhoto();
    static QString getPhotoInvalid();

private:
    int m_id;
    QString m_name;
    QString m_photo;
    const static inline int m_idAddRoom = -1;
    const static inline QString m_nameAddRoom = "Add room";
    const static inline QString m_photoAddRoom = "add.png";
    const static inline QStringList m_listValidPhotos = {
        "living_room.jpg",
        "bedroom.jpg",
        "lobby.jpg",
        "kitchen.jpg",
        "pantry.jpg",
        "pbathroom.jpg",
        "toilet.jpg",
        "garage.jpg"
    };
    const static inline QString m_photoInvalid = "unknown.png";
};

#endif // ROOM_H
