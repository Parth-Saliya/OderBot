<?php

defined('BASEPATH') OR exit('No direct script access allowed');

class Totalpro {

    public function __construct() {
        $this->CI = & get_instance();
    }

    //-------- tbl get --------------------
    /*
     * $tbl = table name
     * $order_col = order by column name
     * $sort = ASC, DESC
     */
    public function getTbl_result($tbl, $order_col = null, $sort = null) {
        if (!empty($order_col) && !empty($sort)) {
            $this->CI->db->order_by($order_col, $sort);
        }
        $res = $this->CI->db->get($tbl);
        return $res->result();
    }

    public function getTbl_row($tbl, $order_col = null, $sort = null) {
        if (!empty($order_col) && !empty($sort)) {
            $this->CI->db->order_by($order_col, $sort);
        }
        $res = $this->CI->db->get($tbl);
        return $res->row();
    }

    public function getTbl_result_array($tbl, $order_col = null, $sort = null) {
        if (!empty($order_col) && !empty($sort)) {
            $this->CI->db->order_by($order_col, $sort);
        }
        $res = $this->CI->db->get($tbl);
        return $res->result_array();
    }

    public function getTbl_row_array($tbl) {
        $res = $this->CI->db->get($tbl);
        return $res->row_array();
    }

    public function getTbl_Max_row($tbl, $id) {
        $this->CI->db->select_max($id);
        $res = $this->CI->db->get($tbl);
        return $res->row();
    }

    //---------- tbl get where ------------------------
    /*
     * $tbl = table name
     * $ar = array(
     * 'column name 1'=>'value',
     * 'column name 2'=>'value',
     * )
     * 
     * $order_col = order by column name
     * $sort = ASC, DESC
     * 
     */

    public function getTbl_where_result($tbl, $ar, $order_col = null, $sort = null) {

        if (!empty($order_col) && !empty($sort)) {
            $this->CI->db->order_by($order_col, $sort);
        }
        foreach ($ar as $a => $k) {
            $this->CI->db->where($a, $k);
        }

        $res = $this->CI->db->get($tbl);
        return $res->result();
    }

    public function getTbl_where_row($tbl, $ar) {

        $res = $this->CI->db->get_where($tbl, $ar);
        return $res->row();
    }

    public function getTbl_where_result_array($tbl, $ar, $order_col = null, $sort = null) {
        if (!empty($order_col) && !empty($sort)) {
            $this->CI->db->order_by($order_col, $sort);
        }
        $res = $this->CI->db->get_where($tbl, $ar);
        return $res->result_array();
    }

    public function getTbl_where_row_array($tbl, $ar) {
        $res = $this->CI->db->get_where($tbl, $ar);
        return $res->row_array();
    }

    // -------------- check unique ---------------
    /*
     * $tbl = table name
     * $col = column name
     * $where = pass value of checkunique or not
     * 
     */

    public function checkUnique($tbl, $col, $where) {
        $this->CI->db->where($col, $where);
        $res = $this->CI->db->get($tbl);
        if ($res->num_rows() > 0) {
            return false;
        } else {
            return true;
        }
    }

    public function checkUnique_bill($tbl, $col, $where, $store) {
        $resss = $this->create_slug($where, $store);
//        print_r($resss);die;
        return $resss;
    }

    public function create_slug($name, $store) {
        $t = & get_instance();
        $slug = url_title($name);
        $slug = strtoupper($slug);
        $i = 'A';
        $params = array();
        $params['as_bill_no'] = $slug;
        $params['as_store_id'] = $store;
        $key = NULL;
        $value = NULL;
        $table = 'store_sale';
        if ($key)
            $params["$key !="] = $value;

        while ($t->db->where($params)->get($table)->num_rows()) {
            if (!preg_match('/-{1}[A-Z]+$/', $slug))
                $slug = $name . ++$i;
            else
                $slug = preg_replace('/[A-Z]+$/', ++$i, $slug);

            $params ['as_bill_no'] = $slug;
        }

        return $slug;
    }

    /*
     * $tbl = table name
     * $col = column name
     * $where = pass value of checkunique or not
     * $col_id = table main auto increment ID column name
     * $id = pass value of table row of ID
     * 
     */

    public function checkUniqueEdit($tbl, $col, $where, $col_id, $id) {
        $this->CI->db->where($col, $where);
        $this->CI->db->where($col_id . ' <>', $id);
        $res = $this->CI->db->get($tbl);
        if ($res->num_rows() > 0) {
            return false;
        } else {
            return true;
        }
    }

    //------------ check empny or not --------------
    /*
     * Value = This is Post value which is pass using post method
     * 
     * $array = array(
     *  'Value 1', 'Value 2', 'Value 3'...
     * )
     * 
     */
    public function checkEmptyOrNot($array) {
        $ret = true;
        foreach ($array as $v) {
            if (empty($v)) {
                $ret = false;
                break;
            }
        }
        return $ret;
    }

    //------------------ display alert message on front ---------------
    /*
     * $flag = error, success
     * $msg = message display in title
     * $msg2 = message display in description
     */
    public function ErrorMessage($flag, $msg, $msg2) {
        $d = array(
            'flag' => $flag,
            'msg' => $msg,
            'msg2' => $msg2
        );

        echo json_encode($d);
        die;
    }

    //------------------ upload single image ---------------
    /*
     * $name = file input name
     * $imageRequired = true, false
     */

    public function uploadSingleImage($name, $imageRequired) {

        $check = true;
        if ($imageRequired) {
            if (isset($_FILES[$name]['name']) && empty($_FILES[$name]['name'])) {
                $check = false;
                $this->ErrorMessage('error', 'Error!', 'Please Select file!');
            }
        }

        if ($check) {

            if (isset($_FILES[$name]['name']) && !empty($_FILES[$name]['name'])) {

                $_FILES['userFile']['name'] = $_FILES[$name]['name'];
                $_FILES['userFile']['type'] = $_FILES[$name]['type'];
                $_FILES['userFile']['tmp_name'] = $_FILES[$name]['tmp_name'];
                $_FILES['userFile']['error'] = $_FILES[$name]['error'];
                $_FILES['userFile']['size'] = $_FILES[$name]['size'];

                $uploadPath = 'upload/';
                $config['upload_path'] = $uploadPath;
                $config['allowed_types'] = '*';
                $config['file_name'] = time() . rand();
                $config['width'] = 50;
                $config['height'] = 50;
                $this->CI->load->library('image_lib', $config);
                $this->CI->image_lib->resize();

                $this->CI->load->library('upload', $config);
                $this->CI->upload->initialize($config);
                if ($this->CI->upload->do_upload('userFile')) {
                    $fileData = $this->CI->upload->data();

                    $uploadData['image'] = $fileData['file_name'];
                }
                return $uploadData['image'];
            } else {
                return '';
            }
        }
    }

    //------------------ remove files from upload folder ---------------
    /*
     * $tbl = table name
     * $col_id = table column name
     * $id_val = pass id value
     * $file_col_name = file store column name
     */
    function deleteFiles($tbl, $col_id, $id_val, $file_col_name) {
        $this->CI->db->select('*');
        $this->CI->db->from($tbl);
        $this->CI->db->where_in($col_id, $id_val);
        $query = $this->CI->db->get();
        $del_data = $query->result();
        foreach ($del_data as $v) {
            if ($v->$file_col_name != '') {
                if (file_exists('uploads/' . $v->$file_col_name)) {
                    unlink('uploads/' . $v->$file_col_name);
                }
            }
        }
        return true;
    }

    function deleteFiles2($tbl, $where, $file_col_name) {
        $del_data = $this->CI->db->get_where($tbl, $where)->result();

        foreach ($del_data as $v) {
            if ($v->$file_col_name != '') {
                if (file_exists('upload/' . $v->$file_col_name)) {
                    unlink('upload/' . $v->$file_col_name);
                }
            }
        }
        return true;
    }

}
