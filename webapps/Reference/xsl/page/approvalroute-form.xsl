<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:import href="../layout.xsl"/>

    <xsl:template match="/request">
        <xsl:call-template name="layout"/>
    </xsl:template>

    <xsl:template name="_content">
        <xsl:apply-templates select="//document[@entity = 'approvalroute']"/>
    </xsl:template>

    <xsl:template match="document[@entity]">
        <form name="{@entity}" action="" data-edit="{@editable}">
            <header class="content-header">
                <h1 class="header-title">
                    <xsl:value-of select="//captions/approval_route/@caption"/>
                </h1>
                <div class="content-actions">
                    <xsl:apply-templates select="//actionbar"/>
                </div>
            </header>
            <section class="content-body">
                <ul class="nav nav-tabs" role="tablist">
                    <li class="active">
                        <a href="#tabs-1" role="tab" data-toggle="tab">
                            <xsl:value-of select="//captions/properties/@caption"/>
                        </a>
                    </li>
                    <li>
                        <a href="#tabs-2" role="tab" data-toggle="tab">
                            <xsl:value-of select="//captions/blocks/@caption"/>
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
                                    <input type="text" name="name" value="{fields/name}" class="span7" autofocus="true"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="control-label">
                                    <xsl:value-of select="//captions/is_on/@caption"/>
                                </div>
                                <div class="controls">
                                    <input type="checkbox" name="ison" value="true" style="margin-top:10px">
                                        <xsl:if test="fields/ison = 'true'">
                                            <xsl:attribute name="checked">checked</xsl:attribute>
                                        </xsl:if>
                                    </input>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="control-label">
                                    <xsl:value-of select="//captions/category/@caption"/>
                                </div>
                                <div class="controls">
                                    <input type="text" name="category" value="{fields/category}" class="span4" autofocus="true"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="control-label">
                                    <xsl:value-of select="//captions/schema/@caption"/>
                                </div>
                                <div class="controls">
                                    <select name="schema" class="span6">
                                        <xsl:apply-templates select="fields/schema" mode="selected_options_schema"/>
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
                            <a class="btn action_add_route_block" title="Add route block" href="#"  data-action="add_route_block">
                                <span>
                                    <xsl:value-of select="//captions/add_route_block/@caption"/>
                                </span>
                            </a>
                            <a class="btn action_delete_route_block" title="Delete route block" href="#"  data-action="delete_route_block">
                                <span>
                                    <xsl:value-of select="//captions/delete_route_block/@caption"/>
                                </span>
                            </a>
                        </fieldset>


                        <div id="route_blocks_wrapper">
                            <xsl:for-each select="fields/routeblocks/entry">
                                <fieldset class="fieldset">
                                    <legend class="legend">

                                    </legend>
                                    <div class="route_block">
                                        <div class="form-group">
                                            <div class="control-label">
                                                <input type="checkbox" name="route_block_chbox"  style="margin-right:70%; "/> <xsl:value-of select="//captions/type/@caption"/>
                                            </div>
                                            <div class="controls">
                                                <select name="type" class="span6">
                                                    <xsl:variable name="type" select="type"/>
                                                    <xsl:for-each select="//constants[@entity='approvaltype']/entry">
                                                        <option value="{@attrval}">
                                                            <xsl:if test="@attrval = $type">
                                                                <xsl:attribute name="selected">selected</xsl:attribute>
                                                            </xsl:if>
                                                            <xsl:value-of select="."/>
                                                        </option>
                                                    </xsl:for-each>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="control-label">
                                                <xsl:value-of select="//captions/time_limit/@caption"/>
                                            </div>
                                            <div class="controls">
                                                <input type="number" name="timelimit" value="{timelimit}" class="span1" autofocus="true"/>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="control-label">
                                                <xsl:value-of select="//captions/require_comment_if_no/@caption"/>
                                            </div>
                                            <div class="controls">
                                                <input type="checkbox" name="requirecommentifno" value="'true'" style="margin-top:10px">
                                                    <xsl:if test="requirecommentifno = 'true'">
                                                        <xsl:attribute name="checked">checked</xsl:attribute>
                                                    </xsl:if>
                                                </input>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="control-label">
                                                <xsl:value-of select="//captions/approvers/@caption"/>
                                            </div>
                                            <div class="controls">
                                                <select name="approvers" class="span7">
                                                    <xsl:apply-templates select="approvers/entry" mode="selected_options" />
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </fieldset>
                            </xsl:for-each>
                        </div>
                    </div>
                </div>
            </section>
            <input type="hidden" id="fsid" name="fsid" value="{//fsid}"/>
            <div class="route_block_blank" style="visibility:hidden; margin-top:7px">
                <div  class="form-group">
                    <div class="control-label">
                        <input type="checkbox" name="route_block_chbox"  style="margin-right:70%; "/> <xsl:value-of select="//captions/type/@caption"/>
                    </div>
                    <div class="controls">
                        <input type="text" name="route_block_type" value="{type}" class="span4" autofocus="true"/>
                    </div>
                </div>
                <div class="form-group">
                    <div class="control-label">
                        <xsl:value-of select="//captions/time_limit/@caption"/>
                    </div>
                    <div class="controls">
                        <input type="number" name="timelimit" value="{timelimit}" class="span3" autofocus="true"/>
                    </div>
                </div>
                <div class="form-group">
                    <div class="control-label">
                        <xsl:value-of select="//captions/require_comment_if_no/@caption"/>
                    </div>
                    <div class="controls">
                        <input type="checkbox" name="requirecommentifno" value="'true'" style="margin-top:10px">
                            <xsl:if test="requirecommentifno = 'true'">
                                <xsl:attribute name="checked">checked</xsl:attribute>
                            </xsl:if>
                        </input>
                    </div>
                </div>
                <div class="form-group">
                    <div class="control-label">
                        <xsl:value-of select="//captions/approvers/@caption"/>
                    </div>
                    <div class="controls" id="approvers_controls">
                    </div>
                </div>

            </div>
        </form>

    </xsl:template>

    <xsl:template match="*" mode="selected_options">
        <option value="{@id}" selected="selected">
            <xsl:value-of select="name"/>
        </option>
    </xsl:template>

    <xsl:template match="*" mode="selected_options_schema">
        <xsl:for-each select="//constants[@entity='approvalschematype']/entry">
            <option value="{@attrval}">
                <xsl:if test="@attrval = //fields/schema">
                    <xsl:attribute name="selected">selected</xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
            </option>
        </xsl:for-each>
    </xsl:template>

    <xsl:template match="*" mode="selected_options_approvaltype">
        <xsl:for-each select="//constants[@entity='approvaltype']/entry">
            <option value="{@attrval}">
                <xsl:if test="@attrval = //fields/schema">
                    <xsl:attribute name="selected">selected</xsl:attribute>
                </xsl:if>
                <xsl:value-of select="."/>
            </option>
        </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>
