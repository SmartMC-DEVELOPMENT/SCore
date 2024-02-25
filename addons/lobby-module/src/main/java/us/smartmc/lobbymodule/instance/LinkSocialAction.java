package us.smartmc.lobbymodule.instance;

import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public abstract class LinkSocialAction implements ILinkSocial {

    public static final String DEFAULT_USERNAME_REGEX =  "[a-zA-Z0-9_.-]+";

    private final LinkSocialInfo info;
    private final LinkSocialType type;

    public LinkSocialAction(LinkSocialType type) {
        this.type = type;
        this.info = getClass().getDeclaredAnnotation(LinkSocialInfo.class);
    }

    @Override
    public String getUsernameFromUrl(String url) {
        for (String regexString : getValidRegexPatterns()) {
            if (regexString.startsWith("@")) continue;
            Pattern regex = Pattern.compile(regexString);
            Matcher matcher = regex.matcher(url);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        return null;
    }

    @Override
    public String[] getValidRegexPatterns() {
        return new String[]{
                DEFAULT_USERNAME_REGEX
        };
    }

    @Override
    public String getFormattedLink(String username) {
        return String.format(info.linkFormat(), username);
    }
}
