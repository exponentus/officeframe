<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:import href="../layout.xsl"/>

    <xsl:template match="/request">
        <xsl:call-template name="layout"/>
    </xsl:template>

    <xsl:template name="_content">
        <xsl:apply-templates select="//document[@entity = 'role']"/>
    </xsl:template>

    <xsl:template match="document[@entity]">
        <form name="{@entity}" action="" data-edit="{@editable}">
            <header class="content-header">
                <h1 class="header-title">
                    <xsl:value-of select="//captions/role/@caption"/>
                </h1>
                <div class="content-actions">
                    <xsl:apply-templates select="//actionbar"/>
                </div>
            </header>
            <section class="content-body">
                <ul class="nav nav-tabs" role="tablist">
                    <li class="active">
                        <a href="#tabs-1" role="tab" data-toggle="tab">
                            Properties
                        </a>
                    </li>
                    <li>
                        <a href="#tabs-2" role="tab" data-toggle="tab">
                            Localized descriptions
                        </a>
                    </li>
                </ul>
                <div class="tab-content">
                <div role="tabpanel" class="tab-pane active" id="tabs-1">
                <fieldset class="fieldset">
                    <div class="form-group">
                        <div class="control-label">
                            <xsl:value-of select="//captions/name/@caption"/>
                        </div>
                        <div class="controls">
                            <select name="name" class="span6">
                                <xsl:apply-templates select="//query[@entity = 'roles']/entry" mode="names_entry"/>
                            </select>
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
                </div>
                    <div role="tabpanel" class="tab-pane" id="tabs-2">
                        <fieldset class="fieldset">
                            <legend class="legend" style="background:#fff">
                                Localized descriptions
                            </legend>
                            <xsl:for-each select="fields/localizeddescr/entry">
                                <div class="form-group">
                                    <div class="control-label">
                                        <xsl:value-of select="./@id"/>
                                    </div>
                                    <div class="controls">
                                        <textarea type="text" value="{.}" name="{lower-case(./@id)}localizeddescr" class="span7" colspan="2">
                                            <xsl:value-of select="."/>
                                        </textarea>
                                    </div>
                                </div>
                            </xsl:for-each>
                        </fieldset>
                    </div>
                </div>
            </section>
            <input type="hidden" id="fsid" name="fsid" value="{//fsid}"/>
        </form>
    </xsl:template>

    <xsl:template match="*" mode="names_entry">
        <option value="{viewcontent/entry}">
            <xsl:if test="viewcontent/entry = //fields/name">
                <xsl:attribute name="selected">selected</xsl:attribute>
            </xsl:if>
            <xsl:value-of select="viewcontent/entry"/>
        </option>
    </xsl:template>

</xsl:stylesheet>
