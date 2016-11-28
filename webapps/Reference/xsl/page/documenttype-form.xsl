<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:import href="../layout.xsl"/>

    <xsl:template match="/request">
        <xsl:call-template name="layout"/>
    </xsl:template>

    <xsl:template name="_content">
        <xsl:apply-templates select="//document[@entity = 'documenttype']"/>
    </xsl:template>

    <xsl:template match="document[@entity]">
        <form name="{@entity}" action="" data-edit="{@editable}">
            <header class="content-header">
                <h1 class="header-title">
                    <xsl:value-of select="//captions/doc_type/@caption"/>
                </h1>
                <div class="content-actions">
                    <xsl:apply-templates select="//actionbar"/>
                </div>
            </header>
            <section class="content-body">
                <fieldset class="fieldset">
                    <div class="form-group">
                        <div class="control-label">
                            <xsl:value-of select="//captions/name/@caption"/>
                        </div>
                        <div class="controls">
                            <input type="text" name="name" value="{fields/name}" class="span7" autofocus="true"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="control-label">
                            <xsl:value-of select="//captions/category/@caption"/>
                        </div>
                        <div class="controls">
                            <ul class="ul-category">
                                <xsl:apply-templates select="//query[@entity='category']/entry" mode="radio_category"/>
                                <li class="list-style-none">
                                    <input type="radio" name="category" id="custom_category" value="" style="vertical-align:top; margin-top:4px"/>
                                    <input type="text" style="margin-top:3px" id="custom_category_input" value="" placeholder="{//captions/new_category/@caption}" class="span5" autofocus="false"/>
                                </li>
                            </ul>
                        </div>
                    </div>
                </fieldset>
                <fieldset class="fieldset">
                    <legend class="legend">
                        <xsl:value-of select="//captions/localized_names/@caption"/>
                    </legend>
                    <xsl:for-each select="fields/localizednames/entry">
                        <div class="form-group">
                            <div class="control-label">
                                <xsl:value-of select="./@id"/>
                            </div>
                            <div class="controls">
                                <input type="text" value="{.}" name="{lower-case(./@id)}localizedname" class="span7"/>
                            </div>
                        </div>
                    </xsl:for-each>
                </fieldset>
            </section>
            <input type="hidden" id="fsid" name="fsid" value="{//fsid}"/>
        </form>
    </xsl:template>

    <xsl:template match="*" mode="select_options">
        <xsl:param name="selected"/>

        <option value="{.}">
            <xsl:if test=". = $selected">
                <xsl:attribute name="selected" select="'selected'"/>
            </xsl:if>
            <xsl:value-of select="text()"/>
        </option>
    </xsl:template>

    <xsl:template match="*" mode="radio_category">
        <xsl:param name="category" select="viewcontent/entry"/>
        <li class="list-style-none">
            <input type="radio" name="category" id="cat_{$category}" value="{.}">
                <xsl:if test="$category = //fields/category">
                    <xsl:attribute name="checked" select="'checked'"/>
                </xsl:if>
            </input>
            <label for="cat_{$category}">
                <xsl:value-of select="$category"/>
            </label>
        </li>
    </xsl:template>

    <xsl:template match="*" mode="selected_options">
        <option value="{@id}" selected="selected">
            <xsl:value-of select="."/>
        </option>
    </xsl:template>
</xsl:stylesheet>
