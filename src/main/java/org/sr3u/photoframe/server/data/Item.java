package org.sr3u.photoframe.server.data;

import com.google.photos.library.v1.proto.MediaItem;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.sr3u.photoframe.server.DateUtil;
import org.sr3u.photoframe.server.ImageUtil;

import java.util.Date;

@Data
@EqualsAndHashCode
@ToString
@DatabaseTable(tableName = "MediaItems")
@NoArgsConstructor
public class Item {

    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField
    private String googleID;
    @DatabaseField
    private long creationTimestamp;
    @DatabaseField
    private long validUntil;
    @DatabaseField
    private String fileName;
    @DatabaseField
    private Date creationDate;
    @DatabaseField
    private String mimeType;
    @DatabaseField
    private long width;
    @DatabaseField
    private long height;
    @DatabaseField
    private double aspectRatio;


    public Item(MediaItem mediaItem) {
        this.googleID = mediaItem.getId();
        this.creationTimestamp = mediaItem.getMediaMetadata().getCreationTime().getSeconds();
        this.fileName = mediaItem.getFilename();
        this.mimeType = mediaItem.getMimeType();
        this.validUntil = defaultCleanupTimestamp();
        this.width = mediaItem.getMediaMetadata().getWidth();
        this.height = mediaItem.getMediaMetadata().getHeight();
        this.aspectRatio = ImageUtil.aspectRatio(width, height);
    }

    public static long defaultCleanupTimestamp() {
        return DateUtil.timestamp(DateUtil.addDays(new Date(), 7));
    }


}
