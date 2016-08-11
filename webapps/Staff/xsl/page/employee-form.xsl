<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:import href="../layout.xsl"/>
    <xsl:import href="../templates/sharedfields.xsl"/>

    <xsl:template match="/request">
        <xsl:call-template name="layout"/>
    </xsl:template>

    <xsl:template match="entry" mode="roles">
        <xsl:param name="select"/>
        <div>
            <label class="input">
                <input type="checkbox" name="role" value="{@id}">
                    <xsl:if test="@id = $select/entry/@id">
                        <xsl:attribute name="checked" select="checked"/>
                    </xsl:if>
                    <span>
                        <xsl:value-of select="viewcontent/name"/>
                        <!-- <div class="text-muted">
                             <xsl:value-of select="viewcontent/description"/>
                         </div>-->
                    </span>
                </input>
            </label>
        </div>
    </xsl:template>

    <xsl:template match="*" mode="selected_options">
        <option value="{@id}" selected="selected">
            <xsl:value-of select="."/>
        </option>
    </xsl:template>

    <xsl:template name="_content">
        <xsl:apply-templates select="//document[@entity = 'employee']"/>
    </xsl:template>

    <xsl:template match="document[@entity]">
        <form name="{@entity}" action="" data-edit="{@editable}">

            <header class="content-header">
                <h1 class="header-title">
                    <xsl:value-of select="//captions/employee/@caption"/>
                </h1>
                <div class="content-actions">
                    <xsl:apply-templates select="//actionbar"/>
                </div>
            </header>
           <!--   <xsl:value-of select="../fsid"/> -->
            <section class="content-body">
                <fieldset class="fieldset" style="float:left">
                    <div class="form-group">
                        <div class="control-label">
                            <xsl:value-of select="//captions/name/@caption"/>
                        </div>
                        <div class="controls">
                            <input type="text" name="name" value="{fields/name}" class="span6" autofocus="true"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="control-label">
                            <xsl:value-of select="//captions/iin/@caption"/>
                        </div>
                        <div class="controls">
                            <input type="text" name="iin" value="{fields/iin}" class="span4"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="control-label">
                            <xsl:value-of select="//captions/organization/@caption"/>
                        </div>
                        <div class="controls">
                            <select name="organization" class="span6">
                                <xsl:apply-templates select="fields/organization" mode="selected_options"/>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="control-label">
                            <xsl:value-of select="//captions/department/@caption"/>
                        </div>
                        <div class="controls">
                            <select name="department" class="span6">
                                <xsl:apply-templates select="fields/department" mode="selected_options"/>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="control-label">
                            <xsl:value-of select="//captions/position/@caption"/>
                        </div>
                        <div class="controls">
                            <select name="position" class="span6">
                                <xsl:apply-templates select="fields/position" mode="selected_options"/>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="control-label">
                            <xsl:value-of select="//captions/roles/@caption"/>
                        </div>
                        <div class="controls">
                            <xsl:apply-templates select="//query[@entity = 'role']/entry" mode="roles">
                                <xsl:with-param name="select" select="fields/roles"/>
                            </xsl:apply-templates>
                        </div>
                    </div>
                 </fieldset>
                    <span style="display:inline-block" class="upload-photo-wrapper">
                        <div style="width:152px; height:152px; border:1px solid #ccc; position:relative; float:right; margin:12px 10px 0 42px">
                            <label for="upload_photo" class="label-upload-photo">
                                <img style="max-width:150px; max-height:150px" src="p?id=employee-form&amp;docid={//document[@entity='employee']/@docid}&amp;fsid={//fsid}&amp;avatar=1"/>
                                <span class="go-top">
                                    Обновить фотографию
                                    <br/>
                                    <br/>
                                    <img src="img/image-2-48.png"/>
                                </span>
                            </label>
                            <span class="delete-photo" data-docid="{//document[@entity='employee']/@docid}" data-fsid="{//fsid}">
                                <img src="img/x-mark-16.png" style="width:8px;"/>
                            </span>
                        </div>
                    </span>
                    <fieldset class="fieldset">
                        <legend class="legend">
                            <xsl:value-of select="//captions/credentials/@caption"/>
                        </legend>
                        <xsl:if test="@docid = 'null'">
                            <div class="form-group">
                                <div class="control-label"></div>
                                <div class="controls">
                                    <label class="btn btn-sm">
                                        <input type="checkbox" name="reguser" id="reguser" autocomplete="off">
                                            <xsl:if test="fields/reguser = 'on'">
                                                <xsl:attribute name="checked">checked</xsl:attribute>
                                            </xsl:if>
                                            <span>
                                                Зарегистрировать учетную запись
                                            </span>
                                        </input>
                                    </label>
                                </div>
                            </div>
                        </xsl:if>
                        <div class="form-group">
                            <div class="control-label">
                                <xsl:value-of select="//captions/login_name/@caption"/>
                            </div>
                            <div class="controls">
                                <input type="text" name="login" value="{fields/login}" class="span5" autocomplete="off">
                                    <xsl:if test="not(fields/reguser) or @docid != 'null'">
                                        <xsl:attribute name="disabled">disabled</xsl:attribute>
                                    </xsl:if>
                                </input>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="control-label">
                                <xsl:value-of select="//captions/email/@caption"/>
                            </div>
                            <div class="controls">
                                <input type="email" name="email" value="{fields/email}" class="span4"
                                       autocomplete="off">
                                    <xsl:if test="not(fields/reguser) or @docid != 'null'">
                                        <xsl:attribute name="disabled">disabled</xsl:attribute>
                                    </xsl:if>
                                </input>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="control-label">
                                <xsl:value-of select="//captions/password/@caption"/>
                            </div>
                            <div class="controls">
                                <input type="password" name="pwd" value="{fields/password}" class="span4"
                                       autocomplete="off">
                                    <xsl:if test="not(fields/reguser) or @docid != 'null'">
                                        <xsl:attribute name="disabled">disabled</xsl:attribute>
                                    </xsl:if>
                                </input>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="control-label">
                                <xsl:value-of select="//captions/password_confirm/@caption"/>
                            </div>
                            <div class="controls">
                                <input type="password" name="pwd_confirm" value="{fields/reenterpassword}" class="span4"
                                       autocomplete="off">
                                    <xsl:if test="not(fields/reguser) or @docid != 'null'">
                                        <xsl:attribute name="disabled">disabled</xsl:attribute>
                                    </xsl:if>
                                </input>
                            </div>
                        </div>
                    </fieldset>

                    <fieldset class="fieldset">
                        <legend class="legend">
                            <xsl:value-of select="//captions/upload_photo/@caption"/>
                        </legend>
                        <div class="control-label">
                        </div>
                        <div class="controls">
                            <xsl:call-template name="upload-files">
                                <xsl:with-param name="input-name" select="'avatar'"/>
                            </xsl:call-template>
                        </div>
                    </fieldset>
            </section>
        </form>
    </xsl:template>

</xsl:stylesheet>
