# 后续开发步骤指南

## 当前项目状态

✅ **已完成** (约占总工作量的 50%):
1. 项目初始化与基础架构
2. 认证授权模块(登录、Token管理、操作日志)
3. 用户管理模块(管理员CRUD)
4. 系统管理模块(角色、权限、日志)
5. 数据库设计(6张核心表)
6. 完整文档(12份)

⏳ **待完成** (约占总工作量的 50%):
- 订单管理模块
- 前端项目
- 测试
- 部署

---

## 推荐开发顺序

### 第一优先级: 前端登录页面 ⭐⭐⭐

**为什么优先?**
- 后端核心模块已完成，需要前端界面验证
- 提供可视化的登录界面
- 为后续页面开发打基础

**开发内容:**
1. Vue 3项目初始化
2. Element Plus集成
3. 登录页面
4. 路由配置
5. Axios封装

**预估工作量:** 1-2天

**开发步骤:**
```bash
# 1. 创建前端项目
npm create vite@latest community-admin-frontend -- --template vue-ts
cd community-admin-frontend

# 2. 安装依赖
npm install
npm install element-plus
npm install @element-plus/icons-vue
npm install axios
npm install pinia
npm install vue-router

# 3. 开发登录页面
# 4. 配置路由
# 5. 封装HTTP请求
```

### 第二优先级: 订单管理模块 ⭐⭐

**开发内容:**
1. 订单表设计
2. 订单查询
3. 订单详情
4. 前端页面

**预估工作量:** 3-4天

### 第三优先级: 其他功能 ⭐

- 数据统计
- 内容审核
- 商品管理
- 商家管理

---

## 详细开发指南

### 一、系统管理模块开发指南

#### 1.1 管理员管理

**步骤1: 创建Query DTO**
```java
// AdminUserQuery.java
@Data
@EqualsAndHashCode(callSuper = true)
public class AdminUserQuery extends PageQuery {
    private String keyword;     // 搜索关键词
    private Integer status;     // 状态
    private String startTime;   // 开始时间
    private String endTime;     // 结束时间
}
```

**步骤2: 创建Response VO**
```java
// AdminUserVO.java
@Data
public class AdminUserVO implements Serializable {
    private Long id;
    private String username;
    private String realName;
    private String email;
    private String phone;
    private Integer status;
    private LocalDateTime lastLoginTime;
    private List<String> roleNames;  // 角色名称列表
}
```

**步骤3: 实现Service**
```java
@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {
    
    private final AdminUserMapper adminUserMapper;
    private final AdminRoleMapper adminRoleMapper;
    
    @Override
    public PageResult<AdminUserVO> list(AdminUserQuery query) {
        // 构建查询条件
        LambdaQueryWrapper<AdminUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(query.getKeyword()), 
                     AdminUser::getUsername, query.getKeyword())
               .or()
               .like(StringUtils.hasText(query.getKeyword()), 
                     AdminUser::getRealName, query.getKeyword());
        
        // 分页查询
        Page<AdminUser> page = new Page<>(query.getPage(), query.getSize());
        Page<AdminUser> result = adminUserMapper.selectPage(page, wrapper);
        
        // 转换VO
        List<AdminUserVO> voList = result.getRecords().stream()
            .map(this::convertToVO)
            .collect(Collectors.toList());
        
        return PageResult.of(result.getTotal(), voList);
    }
    
    private AdminUserVO convertToVO(AdminUser user) {
        AdminUserVO vo = new AdminUserVO();
        BeanUtils.copyProperties(user, vo);
        
        // 查询角色
        List<AdminRole> roles = adminRoleMapper.selectRolesByUserId(user.getId());
        vo.setRoleNames(roles.stream()
            .map(AdminRole::getRoleName)
            .collect(Collectors.toList()));
        
        return vo;
    }
}
```

**步骤4: 实现Controller**
```java
@RestController
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class AdminUserController {
    
    private final AdminUserService adminUserService;
    
    @OperationLog("查询管理员列表")
    @GetMapping("/list")
    public Result<PageResult<AdminUserVO>> list(@Validated AdminUserQuery query) {
        return Result.success(adminUserService.list(query));
    }
    
    @OperationLog("查询管理员详情")
    @GetMapping("/{id}")
    public Result<AdminUserVO> getById(@PathVariable Long id) {
        return Result.success(adminUserService.getById(id));
    }
    
    @OperationLog("新增管理员")
    @PostMapping
    public Result<Void> add(@Validated @RequestBody AdminUserDTO dto) {
        adminUserService.add(dto);
        return Result.success("新增成功");
    }
    
    @OperationLog("更新管理员")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, 
                               @Validated @RequestBody AdminUserDTO dto) {
        adminUserService.update(id, dto);
        return Result.success("更新成功");
    }
    
    @OperationLog("删除管理员")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        adminUserService.delete(id);
        return Result.success("删除成功");
    }
}
```

---

### 二、前端开发指南

#### 2.1 项目初始化

```bash
# 创建项目
npm create vite@latest community-admin-frontend -- --template vue-ts
cd community-admin-frontend

# 安装依赖
npm install
npm install element-plus @element-plus/icons-vue
npm install axios pinia vue-router
npm install sass
```

#### 2.2 目录结构

```
frontend/
├── public/
├── src/
│   ├── api/              # API接口
│   │   └── auth.ts
│   ├── assets/           # 静态资源
│   ├── components/       # 组件
│   ├── layouts/          # 布局
│   │   └── BasicLayout.vue
│   ├── router/           # 路由
│   │   └── index.ts
│   ├── stores/           # 状态管理
│   │   └── user.ts
│   ├── utils/            # 工具
│   │   ├── request.ts    # HTTP封装
│   │   └── storage.ts    # 本地存储
│   ├── views/            # 页面
│   │   ├── login/
│   │   │   └── index.vue
│   │   └── home/
│   │       └── index.vue
│   ├── App.vue
│   └── main.ts
├── index.html
├── package.json
├── tsconfig.json
└── vite.config.ts
```

#### 2.3 核心代码示例

**HTTP请求封装(utils/request.ts):**
```typescript
import axios from 'axios'
import { ElMessage } from 'element-plus'

const service = axios.create({
  baseURL: '/admin-api',
  timeout: 5000
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== 200) {
      ElMessage.error(res.message || '请求失败')
      return Promise.reject(new Error(res.message || 'Error'))
    }
    return res.data
  },
  error => {
    ElMessage.error(error.message)
    return Promise.reject(error)
  }
)

export default service
```

**登录API(api/auth.ts):**
```typescript
import request from '@/utils/request'

export interface LoginRequest {
  username: string
  password: string
}

export interface LoginResponse {
  userId: number
  username: string
  realName: string
  token: string
  roles: string[]
  permissions: string[]
}

export const login = (data: LoginRequest) => {
  return request.post<LoginResponse>('/auth/login', data)
}

export const logout = () => {
  return request.post('/auth/logout')
}
```

**登录页面(views/login/index.vue):**
```vue
<template>
  <div class="login-container">
    <el-form :model="loginForm" :rules="rules" ref="formRef">
      <h2>社区服务后台管理系统</h2>
      <el-form-item prop="username">
        <el-input v-model="loginForm.username" placeholder="用户名" />
      </el-form-item>
      <el-form-item prop="password">
        <el-input v-model="loginForm.password" type="password" placeholder="密码" />
      </el-form-item>
      <el-button type="primary" @click="handleLogin" :loading="loading">
        登录
      </el-button>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { login } from '@/api/auth'
import { ElMessage } from 'element-plus'

const router = useRouter()
const formRef = ref()
const loading = ref(false)

const loginForm = ref({
  username: 'admin',
  password: 'admin123'
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  await formRef.value.validate()
  loading.value = true
  try {
    const res = await login(loginForm.value)
    localStorage.setItem('token', res.token)
    ElMessage.success('登录成功')
    router.push('/')
  } finally {
    loading.value = false
  }
}
</script>
```

---

## 开发工具推荐

### 后端工具:
1. **IntelliJ IDEA** - Java开发IDE
2. **Postman** - API测试
3. **Navicat** - 数据库管理
4. **Redis Desktop Manager** - Redis管理

### 前端工具:
1. **VS Code** - 前端开发IDE
2. **Vue DevTools** - Vue调试
3. **Element Plus官方文档** - UI组件参考

---

## 常见问题解决

### Q1: 如何快速生成CRUD代码?
使用MyBatis-Plus代码生成器或IDE插件(如EasyCode)

### Q2: 如何调试?
- 后端: IDEA断点调试
- 前端: Chrome DevTools + Vue DevTools

### Q3: 如何提高开发效率?
1. 使用代码模板
2. 复用现有代码
3. 使用代码生成器
4. 参考优秀开源项目

---

**建议**: 按照本指南的顺序逐步开发,每完成一个模块立即测试,确保质量!

**参考**: 已完成的认证模块代码可作为其他模块的开发模板。
