package com.paveltsikota.webcore.db.types

import java.sql.Timestamp

// why??? cuz for remove args mistaken in constructors and functions, cuz use usages
class DataTypeAlias {
    typealias IdType = Long
    typealias MainType = Long
    typealias SizeType = Long
    typealias ProfileType = Long
    typealias CreatedDateType = Long
    typealias ModifiedDateType = Long
    typealias StartDateType = Long
    typealias FinishDateType = Long
    typealias LastObjectIdType = Long
    typealias GroupIdType = Long
    typealias HashIdType = Long
    typealias JobIdType = Long
    typealias BufferSizeType = Long
    typealias ProgressSizeType = Long

    typealias StartTimestampType = Timestamp
    typealias FinishTimestampType = Timestamp

    typealias ProgressNType = Int
    typealias PriorityType = Int
    typealias DirOrderType = Int
    typealias NType = Int
    typealias DupNType = Int
    typealias PageType = Int
    typealias PageSizeType = Int


    typealias UuidType = String
    typealias PathType = String
    typealias FileNameType = String
    typealias NewFileNameType = String
    typealias HashPathType = String
    typealias HashValueType = String
    typealias TitleType = String
    typealias DescriptionType = String
    typealias RunningTimeType = String

    typealias IsUniqueType = Boolean
    typealias HoldType = Boolean
    typealias GlobalType = Boolean
    typealias DisabledType = Boolean
    typealias FlyHashCalculateType = Boolean
    typealias ProgressShowType = Boolean
    typealias AddOnceType = Boolean

}