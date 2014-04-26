<?php
/**
 *
 * Default view
 *
 * @version             1.0.0
 * @package             Gavern Framework
 * @copyright			Copyright (C) 2010 - 2011 GavickPro. All rights reserved.
 *               
 */
 
// No direct access.
defined('_JEXEC') or die;
// 
$this->layout->generateLayout(20);
//
$app = JFactory::getApplication();
$user = JFactory::getUser();
// getting User ID
$userID = $user->get('id');
// getting params
$option = JRequest::getCmd('option', '');
$view = JRequest::getCmd('view', '');
// defines if login is active
define('GK_LOGIN', $this->API->modules('login'));
// defines if com_users
define('GK_COM_USERS', $option == 'com_users' && ($view == 'login' || $view == 'registration'));
// other variables
$btn_login_text = ($userID == 0) ? JText::_('TPL_GK_LANG_LOGIN') : JText::_('TPL_GK_LANG_LOGOUT');
$tpl_page_suffix = $this->page_suffix != '' ? ' class="'.$this->page_suffix.'"' : '';
?>
<!DOCTYPE html>
<html lang="<?php echo $this->APITPL->language; ?>">
<head>

	<meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1" />
    
   	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=2.0">   
    <jdoc:include type="head" />
    <?php $this->layout->loadBlock('head'); ?>
    <?php $this->layout->loadBlock('cookielaw'); ?>
     
<link rel="canonical" href="http://qoly.jp/index.php/goods/15341-oita-trinita-puma-2012-13">
<script type="text/javascript" src="https://apis.google.com/js/plusone.js">
  {lang: 'ja'}
</script>
</head>
<body<?php echo $tpl_page_suffix; ?><?php if($this->browser->get("tablet") == true) echo ' data-tablet="true"'; ?><?php $this->layout->generateLayoutWidths(); ?><?php echo ' data-loading-translation="'.JText::_('TPL_GK_LANG_SIDEBAR_LOADING').'"'; ?>>	
	<?php if ($this->browser->get('browser') == 'ie7' || $this->browser->get('browser') == 'ie6') : ?>
	<!--[if lte IE 7]>
	<div id="ieToolbar"><div><?php echo JText::_('TPL_GK_LANG_IE_TOOLBAR'); ?></div></div>
	<![endif]-->
	<?php endif; ?>	
	
	<div id="gkPageWrap">	 
	    <section id="gkPageTop">                    	
		    <?php $this->layout->loadBlock('logo'); ?>
		    
		    <?php if($this->API->get('show_menu', 1)) : ?>
		    <div id="gkMainMenu"<?php if($this->API->modules('search')) : ?> class="hasSearch"<?php endif; ?>>
		    	<?php
		    		$this->mainmenu->loadMenu($this->API->get('menu_name','mainmenu')); 
		    	    $this->mainmenu->genMenu($this->API->get('startlevel', 0), $this->API->get('endlevel',-1));
		    	?>   
		    	
		    	<div id="gkMobileMenu">
		    	<?php echo JText::_('TPL_GK_LANG_MOBILE_MENU'); ?>
		    	<select onChange="window.location.href=this.value;">
		    	<?php 
		    	    $this->mobilemenu->loadMenu($this->API->get('menu_name','mainmenu')); 
		    	    $this->mobilemenu->genMenu($this->API->get('startlevel', 0), $this->API->get('endlevel',-1));
		    	?>
		    	</select>
		    	</div>
		    	
		    	<?php if($this->API->modules('inset')) : ?>
		    	<div id="gkTabletMenu">Tablet menu</div>
		    	<?php endif; ?>
		    </div>
		    <?php endif; ?>
	    </section>
	    	    
	    <?php if(count($app->getMessageQueue())) : ?>
	    <jdoc:include type="message" />
	    <?php endif; ?>
	
	    <?php if($this->API->modules('header')) : ?>
	    <section id="gkHeader">
	    	<jdoc:include type="modules" name="header" style="<?php echo $this->module_styles['header']; ?>" />
	    </section>
	    <?php endif; ?>
	
		<?php if($this->API->modules('top')) : ?>
		<section id="gkTop" class="masonry">
			<jdoc:include type="modules" name="top" style="<?php echo $this->module_styles['top']; ?>" />
		</section>
		<?php endif; ?>
	
		<div id="gkPageContent">
			<?php if($this->API->modules('inset')) : ?>
			<aside id="gkInset">
				<jdoc:include type="modules" name="inset" style="<?php echo $this->module_styles['inset']; ?>" />
				
				<?php if($this->API->get('footer_position', 'inset') != 'footer') : ?>
					<?php $this->layout->loadBlock('footer'); ?> 
				<?php endif; ?>
			</aside>
			<?php endif; ?>
		
			<section id="gkPage" class="masonry"> 
		    	<section id="gkContent" class="box">					
					<div>
						<?php if($this->API->modules('mainbody_top')) : ?>
						<section id="gkMainbodyTop">
							<jdoc:include type="modules" name="mainbody_top" style="<?php echo $this->module_styles['mainbody_top']; ?>" />
						</section>
						<?php endif; ?>	
						
						<?php if($this->API->modules('breadcrumb') || $this->getToolsOverride()) : ?>
						<section id="gkBreadcrumb">
							<?php if($this->API->modules('breadcrumb')) : ?>
							<jdoc:include type="modules" name="breadcrumb" style="<?php echo $this->module_styles['breadcrumb']; ?>" />
							<?php endif; ?>
							
							<?php if($this->getToolsOverride()) : ?>
								<?php $this->layout->loadBlock('tools/tools'); ?>
							<?php endif; ?>
						</section>
						<?php endif; ?>
						
						<section id="gkMainbody">
							<?php if(($this->layout->isFrontpage() && !$this->API->modules('mainbody')) || !$this->layout->isFrontpage()) : ?>
								<jdoc:include type="component" />
							<?php else : ?>
								<jdoc:include type="modules" name="mainbody" style="<?php echo $this->module_styles['mainbody']; ?>" />
							<?php endif; ?>
						</section>
						
						<?php if($this->API->modules('mainbody_bottom')) : ?>
						<section id="gkMainbodyBottom">
							<jdoc:include type="modules" name="mainbody_bottom" style="<?php echo $this->module_styles['mainbody_bottom']; ?>" />
						</section>
						<?php endif; ?>
					</div>
		    	</section>
		    	
		    	<?php if($this->API->modules('sidebar')) : ?>
		    	<jdoc:include type="modules" name="sidebar" style="<?php echo $this->module_styles['sidebar']; ?>" />
		    	<?php endif; ?>
		    </section>
		</div>
	    
		<?php if($this->API->modules('bottom')) : ?>
		<section id="gkBottom" class="masonry">
			<jdoc:include type="modules" name="bottom" style="<?php echo $this->module_styles['bottom']; ?>" />
		</section>
		<?php endif; ?>
		
	    <?php $this->layout->loadBlock('footer'); ?> 
	    
	    <?php if($this->API->get('stylearea', '0') == '1') : ?>
	    <div id="gkStyleArea">
	        <a href="#" id="gkColor1"><?php echo JText::_('TPL_GK_LANG_COLOR_1'); ?></a>
	        <a href="#" id="gkColor2"><?php echo JText::_('TPL_GK_LANG_COLOR_2'); ?></a>
	        <a href="#" id="gkColor3"><?php echo JText::_('TPL_GK_LANG_COLOR_3'); ?></a>
	    </div>
	    <?php endif; ?>
	    
	    <?php if($this->API->get('framework_logo', '0') == '1') : ?>
	    <a href="http://www.gavick.com" id="gkFrameworkLogo" title="Gavern Framework">Gavern Framework</a>
	    <?php endif; ?>
    </div>
    	
    <?php $this->layout->loadBlock('tools/login'); ?>
    <div id="gkPopupOverlay"></div>
    
   	<?php $this->layout->loadBlock('social'); ?>
	<jdoc:include type="modules" name="debug" />
</body>
</html>