<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:import href="../layout.xsl"/>

    <xsl:template match="/request">
        <xsl:call-template name="layout">
            <xsl:with-param name="include_head">
                <link rel="stylesheet" href="/SharedResources/vendor/google-code-prettify/prettify.css"/>
                <script src="/SharedResources/vendor/google-code-prettify/prettify.js"></script>
                <style>
                    .panel__body pre { margin: 0; }
                    .controls .btn { vertical-align: middle; }
                    .panel { margin-bottom: 10px; }
                    .panel .panel-toggle { font-size: 13px; position: relative; }
                    .panel.open > .panel__body { max-height: 100%; }
                </style>
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template name="_content">
        <xsl:apply-templates select="//request[@id = 'topic-form']/page/response"/>
    </xsl:template>

    <xsl:template match="response">
        <xsl:variable name="fields" select="content/document/fields"/>
        <form name="{@entity}" action="" data-edit="{@editable}">
            <header class="content-header">
                <h1 class="header-title">
                    <xsl:value-of select="//captions/topic/@caption"/>
                </h1>
                <div class="content-actions">
                    <xsl:apply-templates select="//actionbar"/>
                </div>
            </header>
            <section class="content-body">
                <fieldset class="fieldset">
                    <div class="form-group">
                        <div class="control-label">
                            <xsl:value-of select="//captions/subject/@caption"/>
                        </div>
                        <div class="controls">
                            <input type="text" name="subject" value="{$fields/subject}" class="span7" autofocus="true"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="control-label">
                            <xsl:value-of select="//captions/status/@caption"/>
                        </div>
                        <div class="controls">
                            <input type="text" name="status" value="{$fields/status}" class="span3" autofocus="true"/>
                        </div>
                    </div>
                </fieldset>
            </section>
        </form>
        <template id="tpl_rest_result_panel">
            <div class="panel" data-endpoint="">
                <div class="panel__header blink-anim">
                    <div class="panel-title panel-toggle" data-toggle="panel">
                        <div class="span8 pull-left" style="margin-top:1px;">
                            <span class="input js-title"></span>
                        </div>
                        <button type="button" class="btn js-remove">Удалить</button>
                    </div>
                </div>
                <div class="panel__body">
                    <pre class="prettyprint lang-javascript">loading...</pre>
                </div>
            </div>
        </template>
    </xsl:template>

</xsl:stylesheet>
