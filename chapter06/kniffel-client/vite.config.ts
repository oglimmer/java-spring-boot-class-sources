import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  define: {
    // in dev mode : __API_URL__ is set to "http://localhost:8080"
    // in prod mode : __API_URL__ will be statically replaced by the value of process.env.API_URL at build time!
    __API_URL__: JSON.stringify(process.env.API_URL ?? "http://localhost:8080")
  },
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  }
})
