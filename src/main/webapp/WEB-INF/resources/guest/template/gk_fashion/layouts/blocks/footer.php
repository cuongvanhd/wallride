<?php

// No direct access.
defined('_JEXEC') or die;

?>
<footer class="gkFooter<?php if($this->API->get('footer_position', 'inset') == 'inset'): ?> gkInsetUsed<?php endif; ?>"><?php if($this->API->get('copyrights_small', '') !== '') : ?><small><?php echo $this->API->get('copyrights_small', ''); ?></small><?php endif; ?><?php if($this->API->modules('social')) : ?><div><jdoc:include type="modules" name="social" style="<?php echo $this->module_styles['social']; ?>" /></div><?php endif; ?><?php if($this->API->modules('lang')) : ?><jdoc:include type="modules" name="lang" style="<?php echo $this->module_styles['lang']; ?>" /><?php endif; ?><?php if($this->API->get('copyrights', '') !== '') : ?><p><?php echo $this->API->get('copyrights', ''); ?></p><?php else : ?><p>Template Design &copy; <a href="//www.gavick.com" title="Joomla Templates">Joomla Templates</a> GavickPro. All rights reserved.</p><?php endif; ?></footer>