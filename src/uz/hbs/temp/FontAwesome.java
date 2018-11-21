package uz.hbs.temp;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.GridView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;

import uz.hbs.beans.Action;
import uz.hbs.enums.ActionRight;
import uz.hbs.template.MyPage;

public class FontAwesome extends MyPage {
	private static final long serialVersionUID = 1L;

	public FontAwesome() {
		List<String> cssNames = Arrays.asList("fa-glass", "fa-music", "fa-search", "fa-envelope-o", "fa-heart", "fa-star", "fa-star-o", "fa-user",
				"fa-film", "fa-th-large", "fa-th", "fa-th-list", "fa-check", "fa-times", "fa-search-plus", "fa-search-minus", "fa-power-off",
				"fa-signal", "fa-gear", "fa-cog", "fa-trash-o", "fa-home", "fa-file-o", "fa-clock-o", "fa-road", "fa-download",
				"fa-arrow-circle-o-down", "fa-arrow-circle-o-up", "fa-inbox", "fa-play-circle-o", "fa-rotate-right", "fa-repeat", "fa-refresh",
				"fa-list-alt", "fa-lock", "fa-flag", "fa-headphones", "fa-volume-off", "fa-volume-down", "fa-volume-up", "fa-qrcode", "fa-barcode",
				"fa-tag", "fa-tags", "fa-book", "fa-bookmark", "fa-print", "fa-camera", "fa-font", "fa-bold", "fa-italic", "fa-text-height",
				"fa-text-width", "fa-align-left", "fa-align-center", "fa-align-right", "fa-align-justify", "fa-list", "fa-dedent", "fa-outdent",
				"fa-indent", "fa-video-camera", "fa-photo", "fa-image", "fa-picture-o", "fa-pencil", "fa-map-marker", "fa-adjust", "fa-tint",
				"fa-edit", "fa-pencil-square-o", "fa-share-square-o", "fa-check-square-o", "fa-arrows", "fa-step-backward", "fa-fast-backward",
				"fa-backward", "fa-play", "fa-pause", "fa-stop", "fa-forward", "fa-fast-forward", "fa-step-forward", "fa-eject", "fa-chevron-left",
				"fa-chevron-right", "fa-plus-circle", "fa-minus-circle", "fa-times-circle", "fa-check-circle", "fa-question-circle", "fa-info-circle",
				"fa-crosshairs", "fa-times-circle-o", "fa-check-circle-o", "fa-ban", "fa-arrow-left", "fa-arrow-right", "fa-arrow-up",
				"fa-arrow-down", "fa-mail-forward", "fa-share", "fa-expand", "fa-compress", "fa-plus", "fa-minus", "fa-asterisk",
				"fa-exclamation-circle", "fa-gift", "fa-leaf", "fa-fire", "fa-eye", "fa-eye-slash", "fa-warning", "fa-exclamation-triangle",
				"fa-plane", "fa-calendar", "fa-random", "fa-comment", "fa-magnet", "fa-chevron-up", "fa-chevron-down", "fa-retweet",
				"fa-shopping-cart", "fa-folder", "fa-folder-open", "fa-arrows-v", "fa-arrows-h", "fa-bar-chart-o", "fa-twitter-square",
				"fa-facebook-square", "fa-camera-retro", "fa-key", "fa-gears", "fa-cogs", "fa-comments", "fa-thumbs-o-up", "fa-thumbs-o-down",
				"fa-star-half", "fa-heart-o", "fa-sign-out", "fa-linkedin-square", "fa-thumb-tack", "fa-external-link", "fa-sign-in", "fa-trophy",
				"fa-github-square", "fa-upload", "fa-lemon-o", "fa-phone", "fa-square-o", "fa-bookmark-o", "fa-phone-square", "fa-twitter",
				"fa-facebook", "fa-github", "fa-unlock", "fa-credit-card", "fa-rss", "fa-hdd-o", "fa-bullhorn", "fa-bell", "fa-certificate",
				"fa-hand-o-right", "fa-hand-o-left", "fa-hand-o-up", "fa-hand-o-down", "fa-arrow-circle-left", "fa-arrow-circle-right",
				"fa-arrow-circle-up", "fa-arrow-circle-down", "fa-globe", "fa-wrench", "fa-tasks", "fa-filter", "fa-briefcase", "fa-arrows-alt",
				"fa-group", "fa-users", "fa-chain", "fa-link", "fa-cloud", "fa-flask", "fa-cut", "fa-scissors", "fa-copy", "fa-files-o",
				"fa-paperclip", "fa-save", "fa-floppy-o", "fa-square", "fa-navicon", "fa-reorder", "fa-bars", "fa-list-ul", "fa-list-ol",
				"fa-strikethrough", "fa-underline", "fa-table", "fa-magic", "fa-truck", "fa-pinterest", "fa-pinterest-square",
				"fa-google-plus-square", "fa-google-plus", "fa-money", "fa-caret-down", "fa-caret-up", "fa-caret-left", "fa-caret-right",
				"fa-columns", "fa-unsorted", "fa-sort", "fa-sort-down", "fa-sort-desc", "fa-sort-up", "fa-sort-asc", "fa-envelope", "fa-linkedin",
				"fa-rotate-left", "fa-undo", "fa-legal", "fa-gavel", "fa-dashboard", "fa-tachometer", "fa-comment-o", "fa-comments-o", "fa-flash",
				"fa-bolt", "fa-sitemap", "fa-umbrella", "fa-paste", "fa-clipboard", "fa-lightbulb-o", "fa-exchange", "fa-cloud-download",
				"fa-cloud-upload", "fa-user-md", "fa-stethoscope", "fa-suitcase", "fa-bell-o", "fa-coffee", "fa-cutlery", "fa-file-text-o",
				"fa-building-o", "fa-hospital-o", "fa-ambulance", "fa-medkit", "fa-fighter-jet", "fa-beer", "fa-h-square", "fa-plus-square",
				"fa-angle-double-left", "fa-angle-double-right", "fa-angle-double-up", "fa-angle-double-down", "fa-angle-left", "fa-angle-right",
				"fa-angle-up", "fa-angle-down", "fa-desktop", "fa-laptop", "fa-tablet", "fa-mobile-phone", "fa-mobile", "fa-circle-o",
				"fa-quote-left", "fa-quote-right", "fa-spinner", "fa-circle", "fa-mail-reply", "fa-reply", "fa-github-alt", "fa-folder-o",
				"fa-folder-open-o", "fa-smile-o", "fa-frown-o", "fa-meh-o", "fa-gamepad", "fa-keyboard-o", "fa-flag-o", "fa-flag-checkered",
				"fa-terminal", "fa-code", "fa-mail-reply-all", "fa-reply-all", "fa-star-half-empty", "fa-star-half-full", "fa-star-half-o",
				"fa-location-arrow", "fa-crop", "fa-code-fork", "fa-unlink", "fa-chain-broken", "fa-question", "fa-info", "fa-exclamation",
				"fa-superscript", "fa-subscript", "fa-eraser", "fa-puzzle-piece", "fa-microphone", "fa-microphone-slash", "fa-shield",
				"fa-calendar-o", "fa-fire-extinguisher", "fa-rocket", "fa-maxcdn", "fa-chevron-circle-left", "fa-chevron-circle-right",
				"fa-chevron-circle-up", "fa-chevron-circle-down", "fa-html5", "fa-css3", "fa-anchor", "fa-unlock-alt", "fa-bullseye", "fa-ellipsis-h",
				"fa-ellipsis-v", "fa-rss-square", "fa-play-circle", "fa-ticket", "fa-minus-square", "fa-minus-square-o", "fa-level-up",
				"fa-level-down", "fa-check-square", "fa-pencil-square", "fa-external-link-square", "fa-share-square", "fa-compass", "fa-toggle-down",
				"fa-caret-square-o-down", "fa-toggle-up", "fa-caret-square-o-up", "fa-toggle-right", "fa-caret-square-o-right", "fa-euro", "fa-eur",
				"fa-gbp", "fa-dollar", "fa-usd", "fa-rupee", "fa-inr", "fa-cny", "fa-rmb", "fa-yen", "fa-jpy", "fa-ruble", "fa-rouble", "fa-rub",
				"fa-won", "fa-krw", "fa-bitcoin", "fa-btc", "fa-file", "fa-file-text", "fa-sort-alpha-asc", "fa-sort-alpha-desc",
				"fa-sort-amount-asc", "fa-sort-amount-desc", "fa-sort-numeric-asc", "fa-sort-numeric-desc", "fa-thumbs-up", "fa-thumbs-down",
				"fa-youtube-square", "fa-youtube", "fa-xing", "fa-xing-square", "fa-youtube-play", "fa-dropbox", "fa-stack-overflow", "fa-instagram",
				"fa-flickr", "fa-adn", "fa-bitbucket", "fa-bitbucket-square", "fa-tumblr", "fa-tumblr-square", "fa-long-arrow-down",
				"fa-long-arrow-up", "fa-long-arrow-left", "fa-long-arrow-right", "fa-apple", "fa-windows", "fa-android", "fa-linux", "fa-dribbble",
				"fa-skype", "fa-foursquare", "fa-trello", "fa-female", "fa-male", "fa-gittip", "fa-sun-o", "fa-moon-o", "fa-archive", "fa-bug",
				"fa-vk", "fa-weibo", "fa-renren", "fa-pagelines", "fa-stack-exchange", "fa-arrow-circle-o-right", "fa-arrow-circle-o-left",
				"fa-toggle-left", "fa-caret-square-o-left", "fa-dot-circle-o", "fa-wheelchair", "fa-vimeo-square", "fa-turkish-lira", "fa-try",
				"fa-plus-square-o", "fa-space-shuttle", "fa-slack", "fa-envelope-square", "fa-wordpress", "fa-openid", "fa-institution", "fa-bank",
				"fa-university", "fa-mortar-board", "fa-graduation-cap", "fa-yahoo", "fa-google", "fa-reddit", "fa-reddit-square",
				"fa-stumbleupon-circle", "fa-stumbleupon", "fa-delicious", "fa-digg", "fa-pied-piper", "fa-pied-piper-alt", "fa-drupal", "fa-joomla",
				"fa-language", "fa-fax", "fa-building", "fa-child", "fa-paw", "fa-spoon", "fa-cube", "fa-cubes", "fa-behance", "fa-behance-square",
				"fa-steam", "fa-steam-square", "fa-recycle", "fa-automobile", "fa-car", "fa-cab", "fa-taxi", "fa-tree", "fa-spotify", "fa-deviantart",
				"fa-soundcloud", "fa-database", "fa-file-pdf-o", "fa-file-word-o", "fa-file-excel-o", "fa-file-powerpoint-o", "fa-file-photo-o",
				"fa-file-picture-o", "fa-file-image-o", "fa-file-zip-o", "fa-file-archive-o", "fa-file-sound-o", "fa-file-audio-o", "fa-file-movie-o",
				"fa-file-video-o", "fa-file-code-o", "fa-vine", "fa-codepen", "fa-jsfiddle", "fa-life-bouy", "fa-life-saver", "fa-support",
				"fa-life-ring", "fa-circle-o-notch", "fa-ra", "fa-rebel", "fa-ge", "fa-empire", "fa-git-square", "fa-git", "fa-hacker-news",
				"fa-tencent-weibo", "fa-qq", "fa-wechat", "fa-weixin", "fa-send", "fa-paper-plane", "fa-send-o", "fa-paper-plane-o", "fa-history",
				"fa-circle-thin", "fa-header", "fa-paragraph", "fa-sliders", "fa-share-alt", "fa-share-alt-square", "fa-bomb", "fa-angellist",
				"fa-area-chart", "fa-at", "fa-bell-slash", "fa-bell-slash-o", "fa-bicycle", "fa-binoculars", "fa-birthday-cake", "fa-bus",
				"fa-calculator", "fa-cc", "fa-cc-amex", "fa-cc-discover", "fa-cc-mastercard", "fa-cc-paypal", "fa-cc-stripe", "fa-cc-visa",
				"fa-copyright", "fa-eyedropper", "fa-futbol-o", "fa-google-wallet", "fa-ils", "fa-ioxhost", "fa-lastfm", "fa-lastfm-square",
				"fa-line-chart", "fa-meanpath", "fa-newspaper-o", "fa-paint-brush", "fa-paypal", "fa-pie-chart", "fa-plug", "fa-slideshare",
				"fa-toggle-off", "fa-toggle-on", "fa-trash", "fa-tty", "fa-twitch", "fa-wifi", "fa-yelp", "fa-bed", "fa-buysellads",
				"fa-cart-arrow-down", "fa-cart-plus", "fa-connectdevelop", "fa-dashcube", "fa-diamond", "fa-facebook-official", "fa-forumbee",
				"fa-heartbeat", "fa-hotel", "fa-leanpub", "fa-mars", "fa-mars-double", "fa-mars-stroke", "fa-mars-stroke-h", "fa-mars-stroke-v",
				"fa-medium", "fa-mercury", "fa-motorcycle", "fa-neuter", "fa-pinterest-p", "fa-sellsy", "fa-server", "fa-ship", "fa-shirtsinbulk",
				"fa-simplybuilt", "fa-skyatlas", "fa-street-view", "fa-subway", "fa-train", "fa-transgender", "fa-transgender-alt", "fa-user-plus",
				"fa-user-secret", "fa-user-times", "fa-venus", "fa-venus-double", "fa-venus-mars", "fa-viacoin", "fa-500px", "fa-amazon",
				"fa-balance-scale", "fa-battery-0", "fa-battery-1", "fa-battery-2", "fa-battery-3", "fa-battery-4", "fa-battery-empty",
				"fa-battery-full", "fa-battery-half", "fa-battery-quarter", "fa-battery-three-quarters", "fa-black-tie", "fa-calendar-check-o",
				"fa-calendar-minus-o", "fa-calendar-plus-o", "fa-calendar-times-o", "fa-cc-diners-club", "fa-cc-jcb", "fa-chrome", "fa-clone",
				"fa-commenting", "fa-commenting-o", "fa-contao", "fa-creative-commons", "fa-expeditedssl", "fa-firefox", "fa-fonticons",
				"fa-genderless", "fa-get-pocket", "fa-gg", "fa-gg-circle", "fa-hand-grab-o", "fa-hand-lizard-o", "fa-hand-paper-o", "fa-hand-peace-o",
				"fa-hand-pointer-o", "fa-hand-rock-o", "fa-hand-scissors-o", "fa-hand-spock-o", "fa-hand-stop-o", "fa-hourglass", "fa-hourglass-1",
				"fa-hourglass-2", "fa-hourglass-3", "fa-hourglass-end", "fa-hourglass-half", "fa-hourglass-o", "fa-hourglass-start", "fa-houzz",
				"fa-i-cursor", "fa-industry", "fa-internet-explorer", "fa-map", "fa-map-o", "fa-map-pin", "fa-map-signs", "fa-mouse-pointer",
				"fa-object-group", "fa-object-ungroup", "fa-odnoklassniki", "fa-odnoklassniki-square", "fa-opencart", "fa-opera", "fa-optin-monster",
				"fa-registered", "fa-safari", "fa-sticky-note", "fa-sticky-note-o", "fa-television", "fa-trademark", "fa-tripadvisor", "fa-tv",
				"fa-vimeo", "fa-wikipedia-w", "fa-y-combinator", "fa-yc", "fa-bluetooth", "fa-bluetooth-b", "fa-codiepie", "fa-credit-card-alt",
				"fa-edge", "fa-fort-awesome", "fa-hashtag", "fa-mixcloud", "fa-modx", "fa-pause-circle", "fa-pause-circle-o", "fa-percent",
				"fa-product-hunt", "fa-reddit-alien", "fa-scribd", "fa-shopping-bag", "fa-shopping-basket", "fa-stop-circle", "fa-stop-circle-o",
				"fa-usb");

		ListDataProvider<String> cssNamesProvider = new ListDataProvider<String>(cssNames);
		GridView<String> grid = new GridView<String>("rows", cssNamesProvider) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(Item<String> item) {
				item.add(
						new Label("item", "<i style=\"font-size: 28px;\" class=\"fa " + item.getModelObject() + "\"></i><br>" + item.getModelObject())
								.setEscapeModelStrings(false));
			}

			@Override
			protected void populateEmptyItem(Item<String> item) {
				item.add(new Label("item", ""));
			}
		};
		grid.setColumns(10);

		add(grid);
	}

	@Override
	public boolean isAuthorized(Map<ActionRight, Action> actionMap) {
		return getMySession().isSignedIn();
	}

	@Override
	public String getTitle() {
		return "FontAwesome 4.5.0";
	}

	@Override
	public Class<?> implementedClass() {
		return this.getClass();
	}
}