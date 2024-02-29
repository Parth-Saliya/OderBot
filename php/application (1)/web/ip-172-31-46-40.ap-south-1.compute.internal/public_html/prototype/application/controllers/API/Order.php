<?php

defined('BASEPATH') OR exit('No direct script access allowed');
require FCPATH . 'application/libraries/REST_Controller.php';

class Order extends REST_Controller {

	function __construct() {
		parent::__construct();
		header('Access-Control-Allow-Origin: *');
		$this->load->model('API/Morder', 'm');
	} 
	
	public function Order_post(){
		if (isset($_POST['uId']) && !empty($_POST['uId']) && isset($_POST['sId']) && !empty($_POST['sId']) && isset($_POST['fAmount']) && !empty($_POST['fAmount']) && isset($_POST['pDetail']) && !empty($_POST['pDetail'])) {
			
			$data = $this->m->getOrder();
			
			if ($data) {
				$data['status'] = 'true';
			} else {
				$data['status'] = 'false';
			}
		} else {
			$data['status'] = 'false';
		}
		echo json_encode($data);	
	}

	public function uOrderList_get(){
		if (isset($_GET['uId']) && !empty($_GET['uId'])) {
			
			$data = $this->m->getuOrderList();
			
			if ($data) {
				$result['data'] = $data;
				$result['status'] = 'true';
			} else {
				$result['status'] = 'false';
			}
		} else {
			$result['status'] = 'false';
		}
		echo json_encode($result); 	
	}

	public function sOrderList_get(){
		if (isset($_GET['sId']) && !empty($_GET['sId'])) {
			
			$data = $this->m->getsOrderList();
			
			if ($data) {
				$result['data'] = $data;
				$result['status'] = 'true';
			} else {
				$result['status'] = 'false';
			}
		} else {
			$result['status'] = 'false';
		}
		echo json_encode($result); 	
	}

	public function uOrderDetail_get(){
		if (isset($_GET['oId']) && !empty($_GET['oId']) && isset($_GET['uId']) && !empty($_GET['uId'])) {
			
			$data = $this->m->getuOrderDetail();
			
			if ($data) {
				$data['status'] = 'true';
			} else {
				$data['status'] = 'false';
			}
		} else {
			$data['status'] = 'false';
		}
		echo json_encode($data); 	
	}

	public function sOrderDetail_get(){
		if (isset($_GET['oId']) && !empty($_GET['oId']) && isset($_GET['sId']) && !empty($_GET['sId'])) {
			
			$data = $this->m->getsOrderDetail();
			
			if ($data) {
				$data['status'] = 'true';
			} else {
				$data['status'] = 'false';
			}
		} else {
			$data['status'] = 'false';
		}
		echo json_encode($data); 	
	}

	public function Scan_get(){
		if (isset($_GET['oId']) && !empty($_GET['oId']) && isset($_GET['uId']) && !empty($_GET['uId'])) {
			
			$data = $this->m->getScan();
			
			if ($data) {
				$result['status'] = 'true';
			} else {
				$result['status'] = 'false';
			}
		} else {
			$result['status'] = 'false';
		}
		echo json_encode($result); 	
	}

	public function uOrderCancel_get(){
		if (isset($_GET['oId']) && !empty($_GET['oId']) && isset($_GET['uId']) && !empty($_GET['uId'])) {
			
			$data = $this->m->getuOrderCancel();
			
			if ($data) {
				$result['status'] = 'true';
			} else {
				$result['status'] = 'false';
			}
		} else {
			$result['status'] = 'false';
		}
		echo json_encode($result); 	
	}

	public function Review_post(){
		if (isset($_POST['oId']) && !empty($_POST['oId']) && isset($_POST['pId']) && !empty($_POST['pId']) && isset($_POST['uId']) && !empty($_POST['uId']) && isset($_POST['sId']) && !empty($_POST['sId']) && isset($_POST['qOne']) && !empty($_POST['qOne']) && isset($_POST['qTwo']) && !empty($_POST['qTwo']) && isset($_POST['review']) && !empty($_POST['review'])) {
			
			$data = $this->m->getReview();
			
			if ($data) {
				$result['status'] = 'true';
			} else {
				$result['status'] = 'false';
			}
		} else {
			$result['status'] = 'false';
		}
		echo json_encode($result); 	
	}

	public function ReviewShow_post(){
		if (isset($_POST['oId']) && !empty($_POST['oId']) && isset($_POST['pId']) && !empty($_POST['pId']) && isset($_POST['uId']) && !empty($_POST['uId']) && isset($_POST['sId']) && !empty($_POST['sId'])) {
			
			$data = $this->m->getReviewShow();
			
			if ($data) {
				$data['status'] = 'true';
			} else {
				$data['status'] = 'false';
			}
		} else {
			$data['status'] = 'false';
		}
		echo json_encode($data);
	}

}