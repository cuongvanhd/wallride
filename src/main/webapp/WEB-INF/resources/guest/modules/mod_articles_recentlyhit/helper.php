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

require_once JPATH_SITE.'/components/com_content/helpers/route.php';

abstract class modArticlesRecentlyHitHelper
{
	public static function getList(&$params)
	{
		$db = JFactory::getDbo();
		$query = $db->getQuery(true);

		$query->select(
			'a.id, a.title, a.alias, ' .
			'a.catid, a.created, a.introtext, a.images,' .
			'CASE WHEN a.publish_up = 0 THEN a.created ELSE a.publish_up END as publish_up, ' .
				'a.publish_down, a.access '
		);

		$query->from('#__content AS a');

		$query->select('c.alias AS category_alias');
		$query->join('LEFT', '#__categories AS c ON c.id = a.catid');

		$query->select('rh.lasthit as lasthit');
		$query->join('LEFT', '#__content_recentlyhit as rh ON rh.content_id = a.id');

		$query->where('a.state = 1');

		$nullDate	= $db->Quote($db->getNullDate());
		$nowDate	= $db->Quote(JFactory::getDate()->toMySQL());

		$query->where('(a.publish_up = '.$nullDate.' OR a.publish_up <= '.$nowDate.')');
		$query->where('(a.publish_down = '.$nullDate.' OR a.publish_down >= '.$nowDate.')');
		
		$categoryId = $params->get('catid');
		JArrayHelper::toInteger($categoryId);
		$categoryId = implode(',', $categoryId);
		if (!empty($categoryId)) {
			$query->where('a.catid IN ('.$categoryId.')');
		}
		
		if ($params->get('current', 1)) {
			$option = JRequest::getCmd('option');
			$view   = JRequest::getCmd('view');
			if ($option == 'com_content' && $view == 'article') {
				$query->where('a.id!='.JRequest:: getInt('id'));
			}
		}
		 
		$ordering = $params->get('ordering', 'a.publish_up');
		$query->order('lasthit DESC, ' . $ordering . ((trim($ordering) == 'rand()' || trim($ordering) == 'a.ordering' ) ? '' : ' DESC'));
		
    	$db->setQuery($query, 0, (int) $params->get('count', 5)); 
		
		$items = $db->loadObjectList();
		
		$access = !JComponentHelper::getParams('com_content')->get('show_noauth');
		$authorised = JAccess::getAuthorisedViewLevels(JFactory::getUser()->get('id'));
		
		$date = JFactory::getDate();
		
		if ( count($items) ) {
			foreach ($items as &$item) {
				$item->slug = $item->id.':'.$item->alias;
				$item->catslug = $item->catid.':'.$item->category_alias;
				$item->time_ago = '';
				$item->image = '';

				if ($access || in_array($item->access, $authorised)) {
					$item->link = JRoute::_(ContentHelperRoute::getArticleRoute($item->slug, $item->catslug));
				} else {
					$item->link = JRoute::_('index.php?option=com_users&view=login');
				}
				if ($params->get('time_ago',0) && $item->lasthit) {
					$item->time_ago = modArticlesRecentlyHitHelper::timeAgo(strtotime($date),strtotime($item->lasthit));
				}
				if ($params->get('show_image',0)) :
					$img = "";
					$images = json_decode($item->images);
					if (isset($images->image_intro) and !empty($images->image_intro)) :
						$img = $images->image_intro;
					elseif (isset($images->image_fulltext) and !empty($images->image_fulltext)) :
						$img = $images->image_fulltext;
					else :
						$regex   = "/<img[^>]+src\s*=\s*[\"']\/?([^\"']+)[\"'][^>]*\>/";
						$search  = $item->introtext;
						preg_match ($regex, $search, $matches);
						$images = (count($matches)) ? $matches : array();
						if ( count($images) ) {
						  $img  = $images[1];
						}
					endif;
					
					if ($img) {
						$attribs = array();
						if ($params->get('thumb_image',1)) {
							$img = str_replace(JURI::base(false),'',$img);
							$item->image = modArticlesRecentlyHitHelper::getThumbnail($img,$params,$item->id); 
						} else {
							$width  = $params->get('thumb_width',60);
							$height = $params->get('thumb_height',60);
							if ( $width ) {
								$attribs['width'] = $width;
							}
							if ( $height ) {
								$attribs['height'] = $height;
							}
							$item->image  = JHTML::_('image', $img, '', $attribs);
						}
					}
				endif;
			}
		}

		return $items;
	}
	
		public static function timeAgo($dateto,$datefrom) {
			$etime = $dateto-$datefrom;
			
			if ($etime < 1) {
				return '0 seconds';
			}
			
			$a = array( 12 * 30 * 24 * 60 * 60  =>  'YEAR',
						30 * 24 * 60 * 60       =>  'MONTH',
						24 * 60 * 60            =>  'DAY',
						60 * 60                 =>  'HOUR',
						60                      =>  'MINUTE',
						1                       =>  'SECOND'
						);
			
			foreach ($a as $secs => $str) {
				$d = $etime / $secs;
				if ($d >= 1) {
					$r = round($d);
					return sprintf(JText::_('MOD_RECENTLYHIT_TIME_'.$str.($r > 1 ? 'S' : '').'_AGO'),$r);
			}
		}
	}
	
	public static function getThumbnail($img,&$params,$item_id) 
	{
		
		$width      = $params->get('thumb_width',90);
		$height     = $params->get('thumb_height',90);
		$proportion = $params->get('thumb_proportions','bestfit');
		$img_type   = $params->get('thumb_type','');
		$bgcolor    = hexdec($params->get('thumb_bg','#FFFFFF'));
		
		$img_name   = pathinfo($img, PATHINFO_FILENAME);
		$img_ext    = pathinfo($img, PATHINFO_EXTENSION);
		$img_path   = JPATH_BASE  . '/' . $img;
		$size 	    = @getimagesize($img_path);
		
		$errors = array();
		
		if(!$size) 
		{	
			$errors[] = 'There was a problem loading image ' . $img_name . '.' . $img_ext;
		
		} else {
							
			$sub_folder = '0' . substr($item_id, -1);
							
			if ( $img_type ) {
				$img_ext = $img_type;
			}
	
			$origw = $size[0];
			$origh = $size[1];
			if( ($origw<$width && $origh<$height)) {
				$width = $origw;
				$height = $origh;
			}
			
			$prefix = substr($proportion,0,1) . "_".$width."_".$height."_".$bgcolor."_".$item_id."_";
	
			$thumb_file = $prefix . str_replace(array( JPATH_ROOT, ':', '/', '\\', '?', '&', '%20', ' '),  '_' ,$img_name . '.' . $img_ext);		
			
			$thumb_path = dirname(__FILE__).'/thumbs/' . $sub_folder . '/' . $thumb_file;
			
			$attribs = array();
			
			if(file_exists($thumb_path))	{
				$size = @getimagesize($thumb_path);
				if($size) {
					$attribs['width']  = $size[0];
					$attribs['height'] = $size[1];
				}
			} else {
		
				modArticlesRecentlyHitHelper::calculateSize($origw, $origh, $width, $height, $proportion, $newwidth, $newheight, $dst_x, $dst_y, $src_x, $src_y, $src_w, $src_h);
	
				switch(strtolower($size['mime'])) {
					case 'image/png':
						$imagecreatefrom = "imagecreatefrompng";
						break;
					case 'image/gif':
						$imagecreatefrom = "imagecreatefromgif";
						break;
					case 'image/jpeg':
						$imagecreatefrom = "imagecreatefromjpeg";
						break;
					default:
						$errors[] = "Unsupported image type $img_name.$img_ext ".$size['mime'];
				}
	
				
				if ( !function_exists ( $imagecreatefrom ) ) {
					$errors[] = "Failed to process $img_name.$img_ext. Function $imagecreatefrom doesn't exist.";
				}
				
				$src_img = $imagecreatefrom($img_path);
				
				if (!$src_img) {
					$errors[] = "There was a problem to process image $img_name.$img_ext ".$size['mime'];
				}
				
				$dst_img = ImageCreateTrueColor($width, $height);
				
				// $bgcolor = imagecolorallocatealpha($image, 200, 200, 200, 127);
				
				imagefill( $dst_img, 0,0, $bgcolor);
				if ( $proportion == 'transparent' ) {
					imagecolortransparent($dst_img, $bgcolor);
				}
				
				imagecopyresampled($dst_img,$src_img, $dst_x, $dst_y, $src_x, $src_y, $newwidth, $newheight, $src_w, $src_h);		
				
				switch(strtolower($img_ext)) {
					case 'png':
						$imagefunction = "imagepng";
						break;
					case 'gif':
						$imagefunction = "imagegif";
						break;
					default:
						$imagefunction = "imagejpeg";
				}
				
				if($imagefunction=='imagejpeg') {
					$result = @$imagefunction($dst_img, $thumb_path, 80 );
				} else {
					$result = @$imagefunction($dst_img, $thumb_path);
				}
	
				imagedestroy($src_img);
				if(!$result) {				
					if(!$disablepermissionwarning) {
					$errors[] = 'Could not create image:<br />' . $thumb_path . '.<br /> Check if the folder exists and if you have write permissions:<br /> ' . dirname(__FILE__) . '/thumbs/' . $sub_folder;
					}
					$disablepermissionwarning = true;
				} else {
					imagedestroy($dst_img);
				}
			}
		}
		
		if (count($errors)) {
			JError::raiseWarning(404, implode("\n", $errors));
			return false;
		}
		
		$image = JURI::base(false)."modules/mod_articles_recentlyhit/thumbs/$sub_folder/" . basename($thumb_path);
		
		return  JHTML::_('image', $image, '', $attribs);
    }
	
	public static function calculateSize($origw, $origh, &$width, &$height, &$proportion, &$newwidth, &$newheight, &$dst_x, &$dst_y, &$src_x, &$src_y, &$src_w, &$src_h) {
		
		if(!$width ) {
			$width = $origw;
		}

		if(!$height ) {
			$height = $origh;
		}

		if ( $height > $origh ) {
			$newheight = $origh;
			$height = $origh;
		} else {
			$newheight = $height;
		}
		
		if ( $width > $origw ) {
			$newwidth = $origw;
			$width = $origw;
		} else {
			$newwidth = $width;
		}
		
		$dst_x = $dst_y = $src_x = $src_y = 0;

		switch($proportion) {
			case 'fill':
			case 'transparent':
				$xscale=$origw/$width;
				$yscale=$origh/$height;

				if ($yscale<$xscale){
					$newheight =  round($origh/$origw*$width);
					$dst_y = round(($height - $newheight)/2);
				} else {
					$newwidth = round($origw/$origh*$height);
					$dst_x = round(($width - $newwidth)/2);

				}

				$src_w = $origw;
				$src_h = $origh;
				break;

			case 'crop':

				$ratio_orig = $origw/$origh;
				$ratio = $width/$height;
				if ( $ratio > $ratio_orig) {
					$newheight = round($width/$ratio_orig);
					$newwidth = $width;
				} else {
					$newwidth = round($height*$ratio_orig);
					$newheight = $height;
				}
					
				$src_x = ($newwidth-$width)/2;
				$src_y = ($newheight-$height)/2;
				$src_w = $origw;
				$src_h = $origh;				
				break;
				
 			case 'only_cut':
				// }
				$src_x = round(($origw-$newwidth)/2);
				$src_y = round(($origh-$newheight)/2);
				$src_w = $newwidth;
				$src_h = $newheight;
				
				break; 
				
			case 'bestfit':
				$xscale=$origw/$width;
				$yscale=$origh/$height;

				if ($yscale<$xscale){
					$newheight = $height = round($width / ($origw / $origh));
				}
				else {
					$newwidth = $width = round($height * ($origw / $origh));
				}
				$src_w = $origw;
				$src_h = $origh;	
				
				break;
			}

	}
}
