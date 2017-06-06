<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:import href="../layout.xsl"/>

    <xsl:template match="/request">
        <xsl:call-template name="layout"/>
    </xsl:template>

    <xsl:template name="_content">
        <xsl:apply-templates select="//document[@entity = 'controlType']"/>
    </xsl:template>

    <xsl:template match="document[@entity]">
        <form name="{@entity}" action="" data-edit="{@editable}">
            <header class="content-header">
                <h1 class="header-title">
                    <xsl:value-of select="//captions/control_type/@caption"/>
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
                            <xsl:value-of select="//captions/schema/@caption"/>
                        </div>
                        <div class="controls">
                            <select name="controlschematype" class="span7" required="required">
                                <xsl:apply-templates select="//constants[@entity = 'controlschematype']/entry" mode="select_entry"/>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="control-label">
                            <xsl:value-of select="//captions/default_hours/@caption"/>
                        </div>
                        <div class="controls">
                            <input type="number" name="defaulthours" value="{fields/defaulthours}" class="span1" autofocus="true"/>
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


    <xsl:template match="*" mode="select_entry">
        <option value="{@attrval}">
            <xsl:if test="@attrval = //fields/schema">
                <xsl:attribute name="selected">selected</xsl:attribute>
            </xsl:if>
            <xsl:value-of select="."/>
        </option>
    </xsl:template>
</xsl:stylesheet>
