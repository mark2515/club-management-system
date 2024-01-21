<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="Please enter a name" suffix-icon="el-icon-search" v-model="username"></el-input>
      <el-input style="width: 200px" placeholder="Please enter an email" suffix-icon="el-icon-message" class="ml-5" v-model="email"></el-input>
      <el-input style="width: 200px" placeholder="Please enter an address" suffix-icon="el-icon-position" class="ml-5" v-model="address"></el-input>
      <el-button class="ml-5" type="primary" @click="load">Rearch</el-button>
      <el-button type="warning" @click="reset">Reset</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd">Add<i class="el-icon-circle-plus-outline"></i></el-button>
      <el-popconfirm
          class="ml-5"
          confirm-button-text='Confirm'
          cancel-button-text='Let me think about it'
          icon="el-icon-info"
          icon-color="red"
          title="Are you sure you want to delete these data in bulk?"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference">Bulk Delete<i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>
      <el-upload action="http://localhost:9090/user/import" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">
  	    <el-button type="primary" class="ml-5">Import<i class="el-icon-bottom"></i></el-button>
      </el-upload>

      <el-button type="primary" @click="exp" class="ml-5">Export<i class="el-icon-top"></i></el-button>
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="username" label="User Name" width="140"></el-table-column>
      <el-table-column prop="nickname" label="Nick Name" width="120"></el-table-column>
      <el-table-column prop="email" label="Email"></el-table-column>
      <el-table-column prop="phone" label="Phone"></el-table-column>
      <el-table-column prop="address" label="Address"></el-table-column>
      <el-table-column label="Operation"  width="200" align="center">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)">Edit <i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='Confirm'
              cancel-button-text='Let me think about it'
              icon="el-icon-info"
              icon-color="red"
              title="Are you sure you want to delete this data?"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">Delete <i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog title="User Information" :visible.sync="dialogFormVisible" width="30%" >
      <el-form label-width="100px" size="small">
        <el-form-item label="User Name">
          <el-input v-model="form.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Nick Name">
          <el-input v-model="form.nickname" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Email">
          <el-input v-model="form.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Phone">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Address">
          <el-input v-model="form.address" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">Confirm</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "User",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      username: "",
      email: "",
      address: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: []
    }
  },
  created() {
    this.load()
  },
  methods : {
    load() {
      this.request.get("http://localhost:9090/user/page?pageNum="+this.pageNum+
          "&pageSize="+this.pageSize+"&username="+this.username+
          "&email="+this.email +"&address="+this.address).then(res=> {
        console.log(res)
        this.tableData = res.records
        this.total = res.total
      })
    },
    save() {
      this.request.post("http://localhost:9090/user", this.form).then(res => {
        if (res) {
          this.$message.success("Save Successfully!")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("Save Unsuccessfully!")
        }
      })
    },
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
    },
    handleEdit(row) {
      this.form = row
      this.dialogFormVisible = true
    },
    del(id) {
      this.request.delete("http://localhost:9090/user/" + id).then(res => {
        if (res) {
          this.$message.success("Delete Successfully!")
          this.load()
        } else {
          this.$message.error("Delete Unsuccessfully!")
        }
      })
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)
      this.request.post("http://localhost:9090/user/del/batch", ids).then(res => {
        if (res) {
          this.$message.success("Bulk Delete Successfully!")
          this.load()
        } else {
          this.$message.error("Bulk Delete Unsuccessfully!")
        }
      })
    },
    reset() {
      this.username = ""
      this.email = ""
      this.address = ""
      this.load()
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
    exp() {
      window.open("http://localhost:9090/user/export")
    },
    handleExcelImportSuccess() {
      this.$message.success("Import Successfully!")
      this.load()
    }
  }
}
</script>

<style>
.headerBg {
  background: #eee!important;
}
</style>
