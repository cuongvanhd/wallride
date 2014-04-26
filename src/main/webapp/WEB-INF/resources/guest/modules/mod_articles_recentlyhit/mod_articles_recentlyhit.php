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

// Include the syndicate functions only once
require_once dirname(__FILE__).'/helper.php';

$list = modArticlesRecentlyHitHelper::getList($params);
$moduleclass_sfx = htmlspecialchars($params->get('moduleclass_sfx'));

require JModuleHelper::getLayoutPath('mod_articles_recentlyhit', $params->get('layout', 'default'));
