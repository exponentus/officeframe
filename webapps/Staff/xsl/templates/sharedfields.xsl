<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template name="upload-files">
    <xsl:param name="input-name"/>

    <div class="form-group">
        <button type="button" class="btn btn-upload" id="upload_photo" data-upload="{$input-name}">
            <i class="fa fa-paperclip"></i>
            <span>
                <xsl:value-of select="//captions/attach_file/@caption"/>
            </span>
        </button>
        <div class="attachments" data-upload-files="{$input-name}">
            <xsl:for-each select="fields/attachments/attachment">
                <div class="attachments-file">
                    <a class="file-name" data-file="{filename}" href="{url}">
                        <xsl:value-of select="filename"/>
                    </a>
                    <span class="btn btn-sm btn-link btn-remove-file on-edit">
                        <i class="fa fa-times"></i>
                    </span>
                </div>
            </xsl:for-each>
        </div>
    </div>
</xsl:template>
</xsl:stylesheet>