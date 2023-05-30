#include "room.h"


Room::Room(int id, const QString& name, const QString& photo) : m_id(id), m_name(name)
    ,m_photo(m_listValidPhotos.contains(photo) ? photo : Room::getPhotoInvalid())
{

}

int Room::id() const
{
    return m_id;
}

QString Room::name() const
{
    return m_name;
}

QString Room::photo() const
{
    return m_photo;
}

int Room::getAddRoomId()
{
    return m_idAddRoom;
}

QString Room::getAddRoomName()
{
    return m_nameAddRoom;
}

QString Room::getAddRoomPhoto()
{
    return m_photoAddRoom;
}

QString Room::getPhotoInvalid()
{
    return m_photoInvalid;
}
