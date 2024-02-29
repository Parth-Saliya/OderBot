<?php

defined('BASEPATH') OR exit('No direct script access allowed');
require FCPATH . 'application/libraries/REST_Controller.php';

class Store extends REST_Controller {

	function __construct() {
		parent::__construct();
		header('Access-Control-Allow-Origin: *');
		$this->load->model('API/Mstore', 'm');
	} 

	public function Signup_post() {
		if (isset($_POST['sName']) && !empty($_POST['sName']) && isset($_POST['sAddress']) && !empty($_POST['sAddress']) && isset($_POST['sMobile']) && !empty($_POST['sMobile']) && isset($_POST['sEmail']) && !empty($_POST['sEmail']) && isset($_POST['sPassword']) && !empty($_POST['sPassword']) && isset($_POST['sImage']) && !empty($_POST['sImage'])) {

			$data = $this->m->getSignup();

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
	
	
	public function LatLng_post() {
		if (isset($_POST['sId']) && !empty($_POST['sId']) && isset($_POST['sLat']) && !empty($_POST['sLat']) && isset($_POST['sLng']) && !empty($_POST['sLng'])) {

			$data = $this->m->getLatLng();

			if (!empty($data)) {
				$success = $data;
			} else {
				$success['status'] = 'false';
			}
		} else {
			$success['status'] = 'false';
		}

		echo json_encode($success); 
	}
	
	public function Login_post() {
		if (isset($_POST['sEmail']) && !empty($_POST['sEmail']) && isset($_POST['sPassword']) && !empty($_POST['sPassword'])) {
			$data = $this->m->getlogin();
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
	
	public function Detail_post() {
		if (isset($_POST['sId']) && !empty($_POST['sId'])) {
			$data = $this->m->getDetails(); 
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

	public function List_get() {
			$data = $this->m->getList(); 
			if ($data) {
				$success['data'] = $data;
				$success['status'] = 'true';
			} else {
				$success['status'] = 'false';
			}
		echo json_encode($success);
	}

	public function ProductAdd_post() {
		if (isset($_POST['sId']) && !empty($_POST['sId']) && isset($_POST['pName']) && !empty($_POST['pName']) && isset($_POST['pDesc']) && !empty($_POST['pDesc']) && isset($_POST['pQty']) && !empty($_POST['pQty']) && isset($_POST['pAmount']) && !empty($_POST['pAmount']) && isset($_POST['pImage']) && !empty($_POST['pImage'])) {

			$data = $this->m->getProductAdd();
			
			if ($data) {
				$success['status'] = 'true';
			} else {
				$success['status'] = 'false';
			}
		} else {
			$success['status'] = 'false';
		}

		echo json_encode($success); 
	}

	public function ProductList_get() {
		if (isset($_GET['sId']) && !empty($_GET['sId'])) {

			$data = $this->m->getProductList(); 
			if ($data) {
				$success['data'] = $data;
				$success['status'] = 'true';
			} else {
				$success['status'] = 'false';
			}
		} else {
			$success['status'] = 'false';
		}

		echo json_encode($success);
	}

	public function ProductDelete_post() {
		if (isset($_POST['sId']) && !empty($_POST['sId']) && isset($_POST['pId']) && !empty($_POST['pId'])) {

			$data = $this->m->getProductDelete(); 
			if ($data) {
				$success['status'] = 'true';
			} else {
				$success['status'] = 'false';
			}
		} else {
			$success['status'] = 'false';
		}

		echo json_encode($success);
	}

	public function ProductEdit_post() {
		if (isset($_POST['sId']) && !empty($_POST['sId']) && isset($_POST['pId']) && !empty($_POST['pId']) && isset($_POST['pName']) && !empty($_POST['pName']) && isset($_POST['pDesc']) && !empty($_POST['pDesc']) && isset($_POST['pQty']) && !empty($_POST['pQty']) && isset($_POST['pAmount']) && !empty($_POST['pAmount'])) {

			$data = $this->m->getProductEdit(); 
			if ($data) {
				$success['status'] = 'true';
			} else {
				$success['status'] = 'false';
			}
		} else {
			$success['status'] = 'false';
		}

		echo json_encode($success);
	}

	public function EditProfile_post() {
		if (isset($_POST['sId']) && !empty($_POST['sId']) && isset($_POST['sName']) && !empty($_POST['sName']) && isset($_POST['sAddress']) && !empty($_POST['sAddress']) && isset($_POST['sMobile']) && !empty($_POST['sMobile']) && isset($_POST['sLat']) && !empty($_POST['sLat']) && isset($_POST['sLog']) && !empty($_POST['sLog'])) {
			
			$data = $this->m->getEditProfile();
			
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