<?php
/*------------------------------------------------------------------------
# mod_articles_recentlyhit - Recently Hit Articles
# ------------------------------------------------------------------------
# author    Joomla!Vargas
# copyright Copyright (C) 2010 joomla.vargas.co.cr. All Rights Reserved.
# @license - http://www.gnu.org/licenses/gpl-2.0.html GNU/GPL
# Websites: http://joomla.vargas.co.cr
# Technical Support:  Forum - http://joomla.vargas.co.cr/forum
-------------------------------------------------------------------------*/

// no direct access
defined('_JEXEC') or die;

if ($params->get('style', 1)) {
	$style = ".rh-item {"
		. " clear:both;"
		. " }\n"
		. ".rh-item-image {"
		. " float: left;"
		. " margin:0 7px 5px 0;"
		. " }\n"
		. ".rh-item-title{"
		. " display:block;"
		. " }";
	$doc = JFactory::getDocument();
	$doc->addStyleDeclaration($style);
}
?>
<ul class="recentlyhit<?php echo $moduleclass_sfx; ?>">
<?php if ( count($list) ) :
	foreach ($list as $item) : ?>
	<li class="rh-item">
	<?php if ($item->image): ?>
    	<div class="rh-item-image"><a href="<?php echo $item->link; ?>"><?php echo $item->image; ?></a></div><?php endif; ?>
        <span class="rh-item-text">
			<a href="<?php echo $item->link; ?>" class="rh-item-title"><?php echo $item->title; ?></a>
			<?php if ($item->time_ago) : ?>
        	<small><?php echo $item->time_ago; ?><?php endif; ?></small>
        </span>
	</li>
<?php endforeach; 
endif;?>
</ul>
