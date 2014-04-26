<?php

// no direct access
defined('_JEXEC') or die;

JHtml::addIncludePath(JPATH_COMPONENT .'/helpers');

// Create shortcuts to some parameters.
$params = $this->item->params;
$images = json_decode($this->item->images);
$attribs = json_decode($this->item->attribs);

foreach($attribs as $key => $value) {
    if($value != null) {
    	$params->set($key, $value);
    }
}

$canEdit	= $this->item->params->get('access-edit');
$urls = json_decode($this->item->urls);
$user		= JFactory::getUser();

// URL for Social API
$cur_url = (!empty($_SERVER['HTTPS'])) ? "https://".$_SERVER['SERVER_NAME'].$_SERVER['REQUEST_URI'] : "http://".$_SERVER['SERVER_NAME'].$_SERVER['REQUEST_URI'];
$cur_url = preg_replace('@%[0-9A-Fa-f]{1,2}@mi', '', htmlspecialchars($cur_url, ENT_QUOTES, 'UTF-8'));


// OpenGraph support
$template_config = new JConfig();
$uri = JURI::getInstance();
$article_attribs = json_decode($this->item->attribs, true);

$og_title = $this->escape($this->item->title);
$og_type = 'article';
$og_url = $cur_url;
if (version_compare( JVERSION, '1.8', 'ge' ) && isset($images->image_fulltext) and !empty($images->image_fulltext)) {     $og_image = $uri->root() . htmlspecialchars($images->image_fulltext);
     $pin_image = $uri->root() . htmlspecialchars($images->image_fulltext);
} else {
     $og_image = '';
     preg_match('/src="([^"]*)"/', $this->item->text, $matches);
     
     if(isset($matches[0])) {
     	$pin_image = $uri->root() . substr($matches[0], 5,-1);
     }
}

$pin_image = '';
$og_site_name = $template_config->sitename;
$og_desc = '';


if(isset($article_attribs['og:title'])) {
     $og_title = ($article_attribs['og:title'] == '') ? $this->escape($this->item->title) : $this->escape($article_attribs['og:title']);
     $og_type = $this->escape($article_attribs['og:type']);
     $og_url = $cur_url;
     $og_image = ($article_attribs['og:image'] == '') ? $og_image : $uri->root() . $article_attribs['og:image'];
     $og_site_name = ($article_attribs['og:site_name'] == '') ? $template_config->sitename : $this->escape($article_attribs['og:site_name']);
     $og_desc = $this->escape($article_attribs['og:description']);
}

$doc = JFactory::getDocument();
$doc->setMetaData( 'og:title', $og_title );
$doc->setMetaData( 'og:type', $og_type );
$doc->setMetaData( 'og:url', $og_url );
$doc->setMetaData( 'og:image', $og_image );
$doc->setMetaData( 'og:site_name', $og_site_name );
$doc->setMetaData( 'og:description', $og_desc );

$useDefList = (($params->get('show_author')) or ($params->get('show_category')) or ($params->get('show_parent_category'))
	or ($params->get('show_create_date')) or ($params->get('show_modify_date')) or ($params->get('show_publish_date'))
	or ($params->get('show_hits')));

?>

<article class="item-page<?php echo $this->pageclass_sfx?>">
	<?php  if (isset($images->image_fulltext) and !empty($images->image_fulltext)) : ?>
	<div class="img-fulltext-<?php echo $images->float_fulltext ? $images->float_fulltext : $params->get('float_fulltext'); ?>">
	<img
		<?php if ($images->image_fulltext_caption):
			echo 'class="caption"'.' title="' .$images->image_fulltext_caption .'"';
		endif; ?>
		<?php if (empty($images->float_fulltext)):?>
			style="float:<?php echo  $params->get('float_fulltext') ?>"
		<?php else: ?>
			style="float:<?php echo  $images->float_fulltext ?>"
		<?php endif; ?>
		src="<?php echo $images->image_fulltext; ?>" alt="<?php echo $images->image_fulltext_alt; ?>"/>
	</div>
	<?php endif; ?>
	
	<?php if (!empty($this->item->pagination) AND $this->item->pagination && !$this->item->paginationposition && $this->item->paginationrelative) : ?>
	<?php echo $this->item->pagination; ?>
	<?php endif; ?>
		
	<header>
		<?php if ($params->get('show_page_heading', 1)) : ?>
		<h1><?php echo $this->escape($params->get('page_heading')); ?></h1>
		<?php endif; ?>
		
		<?php if ($params->get('show_title')) : ?>
		<h1>
			<?php if ($params->get('link_titles') && !empty($this->item->readmore_link)) : ?>
				<a href="<?php echo $this->item->readmore_link; ?>">
					<?php echo $this->escape($this->item->title); ?>
				</a>
			<?php else : ?>
				<?php echo $this->escape($this->item->title); ?>
			<?php endif; ?>
		</h1>
		<?php endif; ?>
		
		<?php if ($canEdit ||  $params->get('show_print_icon') || $params->get('show_email_icon') || ($params->get('show_create_date'))) : ?>
		<ul>
			<?php if ($params->get('show_create_date')) : ?>
			<li>
				<time pubdate="<?php echo JHtml::_('date', $this->item->created, JText::_(DATE_W3C)); ?>">
					<?php echo JHtml::_('date', $this->item->created, JText::_('Y/m/d H:II')); ?>
				</time>
			</li>
			<?php endif; ?>	
		
			<?php if ($params->get('show_parent_category') && $this->item->parent_slug != '1:root') : ?>
			<li class="parent-category-name">
				<?php	$title = $this->escape($this->item->parent_title);
				$url = '<a href="'.JRoute::_(ContentHelperRoute::getCategoryRoute($this->item->parent_slug)).'">'.$title.'</a>';?>
				<?php if ($params->get('link_parent_category') and $this->item->parent_slug) : ?>
					<?php echo JText::sprintf('COM_CONTENT_PARENT', $url); ?>
				<?php else : ?>
					<?php echo JText::sprintf('COM_CONTENT_PARENT', $title); ?>
				<?php endif; ?>
			</li>
			<?php endif; ?>

			<?php if ($params->get('show_category')) : ?>
			<li class="category-name">
				<?php $title = $this->escape($this->item->category_title);
				$url = '<a href="'.JRoute::_(ContentHelperRoute::getCategoryRoute($this->item->catslug)).'">'.$title.'</a>';?>
				<?php if ($params->get('link_category') and $this->item->catslug) : ?>
					<?php echo JText::sprintf('COM_CONTENT_CATEGORY', $url); ?>
				<?php else : ?>
					<?php echo JText::sprintf('COM_CONTENT_CATEGORY', $title); ?>
				<?php endif; ?>
			</li>
			<?php endif; ?>
			
			<?php if ($params->get('show_modify_date')) : ?>
			<li class="modified">
				<time datetime="<?php echo JHtml::_('date', $this->item->modified, JText::_(DATE_W3C)); ?>"><?php echo JText::sprintf('COM_CONTENT_LAST_UPDATED', JHtml::_('date', $this->item->modified, JText::_('DATE_FORMAT_LC2'))); ?></time>
			</li>
			<?php endif; ?>
			
			<?php if ($params->get('show_publish_date')) : ?>
			<li class="published">
				<time datetime="<?php echo JHtml::_('date', $this->item->publish_up, JText::_(DATE_W3C)); ?>"><?php echo JText::sprintf('COM_CONTENT_PUBLISHED_DATE_ON', JHtml::_('date', $this->item->publish_up, JText::_('DATE_FORMAT_LC2'))); ?></time>
			</li>
			<?php endif; ?>
			
			<?php if ($params->get('show_author') && !empty($this->item->author )) : ?>
			<li class="createdby">
				<?php $author = $this->item->created_by_alias ? $this->item->created_by_alias : $this->item->author; ?>
				<?php if (!empty($this->item->contactid) && $params->get('link_author') == true): ?>
				<?php
					$needle = 'index.php?option=com_contact&view=contact&id=' . $this->item->contactid;
					$menu = JFactory::getApplication()->getMenu();
					$item = $menu->getItems('link', $needle, true);
					$cntlink = !empty($item) ? $needle . '&Itemid=' . $item->id : $needle;
				?>
					<?php echo JText::sprintf('COM_CONTENT_WRITTEN_BY', JHtml::_('link', JRoute::_($cntlink), $author)); ?>
				<?php else: ?>
					<?php echo JText::sprintf('COM_CONTENT_WRITTEN_BY', $author); ?>
				<?php endif; ?>
			</li>
			<?php endif; ?>
			
			<?php if ($params->get('show_hits')) : ?>
			<li class="hits">
				<?php echo JText::sprintf('COM_CONTENT_ARTICLE_HITS', $this->item->hits); ?>
			</li>
			<?php endif; ?>
			
			<?php if (!$this->print) : ?>
				<?php if ($params->get('show_print_icon')) : ?>
				<li class="print-icon">
				<?php echo JHtml::_('icon.print_popup',  $this->item, $params); ?>
				</li>
				<?php endif; ?>
	
				<?php if ($params->get('show_email_icon')) : ?>
				<li class="email-icon">
				<?php echo JHtml::_('icon.email',  $this->item, $params); ?>
				</li>
				<?php endif; ?>
	
				<?php if ($canEdit) : ?>
				<li class="edit-icon">
				<?php echo JHtml::_('icon.edit', $this->item, $params); ?>
				</li>
				<?php endif; ?>
	
			<?php else : ?>
			<li>
			<?php echo JHtml::_('icon.print_screen',  $this->item, $params); ?>
			</li>
			<?php endif; ?>
		</ul>
		<?php endif; ?>

<div class="socialicon">

<div class="twitter" style="width:90px;">
<a href="https://twitter.com/share" class="twitter-share-button" data-via="Qoly_jp" data-lang="ja">ツイート</a>
<script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0];if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src="//platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");</script>
</div>

<div class="fblike" style="margin-top:-7px;">
<script src="http://connect.facebook.net/ja_JP/all.js#xfbml=1"></script><fb:like href="http://qoly.jp<?php echo $_SERVER['REQUEST_URI'] ;?>" send="false" layout="button_count" width="80" show_faces="true" font="arial"></fb:like>
</div>

<div class="hatena"><a title="Ç±ÇÃÉGÉìÉgÉäÅ[ÇÇÕÇƒÇ»ÉuÉbÉNÉ}Å[ÉNÇ…í«â¡" data-hatena-bookmark-layout="standard" data-hatena-bookmark-title="Qoly Football WebMagazine" class="hatena-bookmark-button" href="http://b.hatena.ne.jp/entry/http://qoly.jp<?php echo $_SERVER['REQUEST_URI'] ;?>"><img style="border: none;" height="20" width="20" alt="Ç±ÇÃÉGÉìÉgÉäÅ[ÇÇÕÇƒÇ»ÉuÉbÉNÉ}Å[ÉNÇ…í«â¡" src="http://b.st-hatena.com/images/entry-button/button-only.gif" /></a>
<script async="async" charset="utf-8" src="http://b.st-hatena.com/js/bookmark_button.js" type="text/javascript"></script>
</div>

<div class="mixi"><a class="mixi-check-button" data-key="bcca4dbc45cd4ab703856751a15bc74f74f312ef" href="http://mixi.jp/share.pl">Check</a>
<script src="http://static.mixi.jp/js/share.js" type="text/javascript"></script>
</div>

<div class="evernote">
<script src="http://static.evernote.com/noteit.js" type="text/javascript"></script>
<a onclick="Evernote.doClip({contentId:'mainbody',providerName:'Football Web Magazine Qoly'}); return false;" href="#"><img alt="Clip to Evernote" src="http://static.evernote.com/article-clipper-jp.png" /></a>
</div>

<div class="googleplus">
<script type="text/javascript" src="https://apis.google.com/js/plusone.js"></script>
<g:plusone></g:plusone>
</div>
<div class="googleads" style="width:550px; text-align:center; padding-top:15px;">

<?php
if(
		strstr($_SERVER['HTTP_USER_AGENT'],"iPhone")
		||strstr($_SERVER['HTTP_USER_AGENT'],"iPod")
		||strstr($_SERVER['HTTP_USER_AGENT'],"Android")
	){
include_once("adsense/adsense1.php");
	}else{
include_once("adsense/adsense2.php");
	}
?>
</div>
	</header>

<?php  if (!$params->get('show_intro')) : ?>
	<?php echo $this->item->event->afterDisplayTitle; ?>
<?php endif; ?>

<?php echo $this->item->event->beforeDisplayContent; ?>

<?php if (isset ($this->item->toc)) : ?>
	<?php echo $this->item->toc; ?>
<?php endif; ?>

<?php if (isset($urls) AND ((!empty($urls->urls_position) AND ($urls->urls_position=='0')) OR  ($params->get('urls_position')=='0' AND empty($urls->urls_position) ))
		OR (empty($urls->urls_position) AND (!$params->get('urls_position')))): ?>
<?php echo $this->loadTemplate('links'); ?>
<?php endif; ?>


<?php if ($params->get('access-view')):?>
<?php
if (!empty($this->item->pagination) AND $this->item->pagination AND !$this->item->paginationposition AND !$this->item->paginationrelative):
	echo $this->item->pagination;
 endif;
?>
<?php echo $this->item->text; ?>

<?php if (isset($urls) AND ((!empty($urls->urls_position)  AND ($urls->urls_position=='1')) OR ( $params->get('urls_position')=='1') )): ?>
<?php echo $this->loadTemplate('links'); ?>
<?php endif; ?>

<?php if (!empty($this->item->pagination) AND $this->item->pagination AND $this->item->paginationposition AND!$this->item->paginationrelative): ?>
<?php echo $this->item->pagination; ?>
<?php endif; ?>

<?php elseif ($params->get('show_noauth') == true and  $user->get('guest') ) : ?>
	<?php echo $this->item->introtext; ?>
	<?php //Optional link to let them register to see the whole article. ?>
	<?php if ($params->get('show_readmore') && $this->item->fulltext != null) :
		$link1 = JRoute::_('index.php?option=com_users&view=login');
		$link = new JURI($link1);?>
		<p class="readmore">
		<a href="<?php echo $link; ?>">
		<?php $attribs = json_decode($this->item->attribs);  ?>
		<?php
		if ($attribs->alternative_readmore == null) :
			echo JText::_('COM_CONTENT_REGISTER_TO_READ_MORE');
		elseif ($readmore = $this->item->alternative_readmore) :
			echo $readmore;
			if ($params->get('show_readmore_title', 0) != 0) :
			    echo JHtml::_('string.truncate', ($this->item->title), $params->get('readmore_limit'));
			endif;
		elseif ($params->get('show_readmore_title', 0) == 0) :
			echo JText::sprintf('COM_CONTENT_READ_MORE_TITLE');
		else :
			echo JText::_('COM_CONTENT_READ_MORE');
			echo JHtml::_('string.truncate', ($this->item->title), $params->get('readmore_limit'));
		endif; ?></a>
		</p>
	<?php endif; ?>
<?php endif; ?>
	
	<?php if (!empty($this->item->pagination) AND $this->item->pagination AND $this->item->paginationposition AND $this->item->paginationrelative): ?>
		<?php echo $this->item->pagination;?>
	<?php endif; ?>

	<gavern:social><div id="gkSocialAPI"></gavern:social>
<div class="socialicon">

<div class="twitter" style="width:90px;">
<a href="https://twitter.com/share" class="twitter-share-button" data-via="Qoly_jp" data-lang="ja" data-count="none">ツイート</a>
<script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0];if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src="//platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");</script>
</div>

<div class="fblike" style="margin-top:-7px;">
<script src="http://connect.facebook.net/ja_JP/all.js#xfbml=1"></script><fb:like href="http://qoly.jp<?php echo $_SERVER['REQUEST_URI'] ;?>" send="true" layout="button_count" width="80" show_faces="true" font="arial"></fb:like>
</div>

<div class="hatena"><a title="Ç±ÇÃÉGÉìÉgÉäÅ[ÇÇÕÇƒÇ»ÉuÉbÉNÉ}Å[ÉNÇ…í«â¡" data-hatena-bookmark-layout="standard" data-hatena-bookmark-title="Qoly Football WebMagazine" class="hatena-bookmark-button" href="http://b.hatena.ne.jp/entry/http://qoly.jp<?php echo $_SERVER['REQUEST_URI'] ;?>"><img style="border: none;" height="20" width="20" alt="Ç±ÇÃÉGÉìÉgÉäÅ[ÇÇÕÇƒÇ»ÉuÉbÉNÉ}Å[ÉNÇ…í«â¡" src="http://b.st-hatena.com/images/entry-button/button-only.gif" /></a>
<script async="async" charset="utf-8" src="http://b.st-hatena.com/js/bookmark_button.js" type="text/javascript"></script>
</div>

<div class="mixi"><a class="mixi-check-button" data-key="bcca4dbc45cd4ab703856751a15bc74f74f312ef" href="http://mixi.jp/share.pl">Check</a>
<script src="http://static.mixi.jp/js/share.js" type="text/javascript"></script>
</div>

<div class="evernote">
<script src="http://static.evernote.com/noteit.js" type="text/javascript"></script>
<a onclick="Evernote.doClip({contentId:'mainbody',providerName:'Football Web Magazine Qoly'}); return false;" href="#"><img alt="Clip to Evernote" src="http://static.evernote.com/article-clipper-jp.png" /></a>
</div>

<div class="googleplus">
<g:plusone size="medium" count="false"></g:plusone></div>

</div>	 <gavern:social></div></gavern:social>
	 
<?php echo $this->item->event->afterDisplayContent; ?>
</article>