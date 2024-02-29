<?php

if (!defined('BASEPATH'))
    exit('No direct script access allowed');

if (!function_exists('admin_path_url')) {

    function admin_path_url() {
        $CI = & get_instance();
        return $CI->config->item('admin_path_url');
    }

}