<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:import href="../layout.xsl"/>

    <xsl:template match="/request">
        <xsl:call-template name="layout"/>
    </xsl:template>

    <xsl:template name="_content">
        <form class="sign-up" id="sign-up" name="sign-up" action="" method="post">
            <h1>
                <xsl:value-of select="//captions/sign_up/@caption"/>
            </h1>
            <label>
                <span>
                    <xsl:value-of select="//captions/fio/@caption"/>
                </span>
                <input class="input" type="text" name="fio" value=""/>
            </label>
            <label>
                <span>
                    <xsl:value-of select="//captions/org/@caption"/>
                </span>
                <input class="input" type="text" name="org" value=""/>
            </label>
            <label>
                <span>
                    <xsl:value-of select="//captions/bin/@caption"/>
                </span>
                <input class="input" type="text" name="orgbin" value=""/>
            </label>
            <label>
                <span>
                    <xsl:value-of select="//captions/user/@caption"/>
                </span>
                <input class="input" type="text" name="login" value=""/>
            </label>
            <label>
                <span>
                    <xsl:value-of select="//captions/email/@caption"/>
                </span>
                <input class="input" type="email" name="email" value=""/>
            </label>
            <label>
                <span>
                    <xsl:value-of select="//captions/comment/@caption"/>
                </span>
                <textarea class="input" name="comment"></textarea>
            </label>
            <button class="btn" type="submit" data-action="sign_up">
                <xsl:value-of select="//captions/sign_up/@caption"/>
            </button>
            <a href="?id=workspace" class="btn-sign-in">
                <xsl:value-of select="//captions/login/@caption"/>
            </a>
        </form>
    </xsl:template>

</xsl:stylesheet>
